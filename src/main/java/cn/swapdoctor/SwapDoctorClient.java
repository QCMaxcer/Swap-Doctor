package cn.swapdoctor;

import net.fabricmc.api.ClientModInitializer;

/** Entry point for the client-only Swap Doctor diagnostics mod. */
public final class SwapDoctorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        cn.swapdoctor.client.SwapDoctorClient.initialize();
    }
}
