package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public final class LivingEntityMixin {
    @Inject(method = "swapHandItems", at = @At("HEAD"))
    private void swapdoctor$recordHandSwap(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (SwapDoctorClient.TRACKER.inInputContext() && client.player == (Object) this) {
            SwapDoctorClient.TRACKER.recordHandSwap(SwapDoctorClient.TRACKER.tick(), System.nanoTime());
        }
    }
}
