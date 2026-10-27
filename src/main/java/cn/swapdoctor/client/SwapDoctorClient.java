package cn.swapdoctor.client;

import cn.swapdoctor.config.ConfigStore;
import cn.swapdoctor.config.SwapDoctorConfig;
import cn.swapdoctor.config.HudDisplayMode;
import cn.swapdoctor.config.HudPosition;
import cn.swapdoctor.config.PositiveDirection;
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
        SwapDoctorScaleHud.register();
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

    public static void setScaleHudEnabled(boolean value) {
        config.scaleHudEnabled = value;
        saveConfig();
    }

    public static void setHudDurationMs(int value) {
        config.hudDurationMs = value;
        config.validate();
        saveConfig();
    }

    public static void setScaleHudDurationMs(int value) {
        config.scaleHudDurationMs = value;
        config.validate();
        saveConfig();
    }

    public static void setHudMode(HudDisplayMode mode) {
        config.hudMode = mode;
        saveConfig();
    }

    public static void setScaleHudMode(HudDisplayMode mode) {
        config.scaleHudMode = mode;
        saveConfig();
    }

    public static void setScaleWidth(int value) {
        config.scaleWidth = value;
        config.validate();
        saveConfig();
    }

    public static void setScaleLengthMultiplier(float value) {
        config.scaleLengthMultiplier = value;
        config.validate();
        saveConfig();
    }

    public static void setScaleThicknessMultiplier(float value) {
        config.scaleThicknessMultiplier = value;
        config.validate();
        saveConfig();
    }

    public static void setPositiveDirection(PositiveDirection value) {
        config.positiveDirection = value;
        saveConfig();
    }

    public static void setHudPosition(HudPosition position) {
        config.setHudPosition(position);
        saveConfig();
    }

    public static void setScalePosition(HudPosition position) {
        config.setScalePosition(position);
        saveConfig();
    }

    public static void resetHudLayout() {
        SwapDoctorConfig defaults = new SwapDoctorConfig();
        config.hudX = defaults.hudX;
        config.hudY = defaults.hudY;
        config.scaleX = defaults.scaleX;
        config.scaleY = defaults.scaleY;
        config.scaleWidth = defaults.scaleWidth;
        saveConfig();
    }

    public static void setShowDecimals(boolean value) {
        config.showDecimals = value;
        saveConfig();
    }

    public static void setShowScaleValues(boolean value) {
        config.showScaleValues = value;
        saveConfig();
    }

    public static void saveConfig() {
        config.validate();
        CONFIG_STORE.save(config);
    }
}
