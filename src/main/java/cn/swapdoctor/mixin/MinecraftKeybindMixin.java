package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class MinecraftKeybindMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void swapdoctor$beginInputContext(CallbackInfo ci) { SwapDoctorClient.TRACKER.beginInputContext(); }
    @Inject(method = "handleKeybinds", at = @At("RETURN"))
    private void swapdoctor$endInputContext(CallbackInfo ci) { SwapDoctorClient.TRACKER.endInputContext(); }
}
