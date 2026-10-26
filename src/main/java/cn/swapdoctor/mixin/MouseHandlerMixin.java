package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public final class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("HEAD"))
    private void swapdoctor$recordHotbarScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        SwapDoctorClient.TRACKER.recordHotbarScroll(vertical,
                SwapDoctorClient.TRACKER.tick(), System.nanoTime());
    }
}
