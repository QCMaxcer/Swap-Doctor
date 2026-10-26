package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Captures gameplay action processing rather than raw mouse button callbacks. */
@Mixin(Minecraft.class)
public final class MinecraftActionMixin {
    @Inject(method = "startAttack", at = @At("HEAD"))
    private void swapdoctor$recordAttack(CallbackInfoReturnable<Boolean> cir) {
        SwapDoctorClient.TRACKER.recordAttack(SwapDoctorClient.TRACKER.tick(), System.nanoTime());
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void swapdoctor$recordUse(CallbackInfo ci) {
        SwapDoctorClient.TRACKER.recordUse(SwapDoctorClient.TRACKER.tick(), System.nanoTime());
    }
}
