package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class MinecraftLifecycleMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void swapdoctor$clearOnScreen(Screen screen, CallbackInfo ci) {
        if (screen != null) SwapDoctorClient.TRACKER.clearState();
    }
    @Inject(method = "setLevel", at = @At("HEAD"))
    private void swapdoctor$clearOnLevel(ClientLevel level, CallbackInfo ci) {
        SwapDoctorClient.TRACKER.clearState();
    }
}
