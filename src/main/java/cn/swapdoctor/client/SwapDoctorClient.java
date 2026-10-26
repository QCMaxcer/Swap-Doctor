package cn.swapdoctor.client;

import cn.swapdoctor.config.ConfigStore;
import cn.swapdoctor.config.SwapDoctorConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class SwapDoctorClient implements ClientModInitializer {
    public static final InputTracker TRACKER = InputTracker.INSTANCE;
    private static final ConfigStore CONFIG_STORE = new ConfigStore();
    private static SwapDoctorConfig config = CONFIG_STORE.load();
    private static boolean initialized;

    @Override public void onInitializeClient() {
        initialize();
    }

    public static void initialize() {
        if (initialized) return;
        initialized = true;
        TRACKER.setThreshold(config.maxDelayMs);
        ClientTickEvents.START_CLIENT_TICK.register(TRACKER::beginTick);
        ClientTickEvents.END_CLIENT_TICK.register(TRACKER::endTick);
        SwapDoctorKeybinds.register();
        SwapDoctorHud.register();
    }

    public static SwapDoctorConfig config() {
        return config;
    }

    public static void setMaxDelayMs(int value) {
        config.maxDelayMs = value;
        config.validate();
        TRACKER.setThreshold(config.maxDelayMs);
        saveConfig();
    }

    public static void setHudEnabled(boolean value) {
        config.hudEnabled = value;
        saveConfig();
    }

    public static void setHudDurationMs(int value) {
        config.hudDurationMs = value;
        config.validate();
        saveConfig();
    }

    public static void saveConfig() {
        config.validate();
        CONFIG_STORE.save(config);
    }
}
