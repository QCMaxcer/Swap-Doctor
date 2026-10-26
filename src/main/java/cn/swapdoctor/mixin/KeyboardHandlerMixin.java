package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Captures physical keyboard timing before Minecraft consumes key mappings. */
@Mixin(KeyboardHandler.class)
public final class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void swapdoctor$recordRawKeyboardInput(long window, int action, KeyEvent event, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS) {
            SwapDoctorClient.TRACKER.recordRawSwitchKey(
                    SwapDoctorClient.TRACKER.tick(), System.nanoTime());
        }
    }
}
