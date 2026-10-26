package cn.swapdoctor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class SwapDoctorClient implements ClientModInitializer {
    public static final InputTracker TRACKER = InputTracker.INSTANCE;
    @Override public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(TRACKER::beginTick);
        ClientTickEvents.END_CLIENT_TICK.register(TRACKER::endTick);
    }
}
