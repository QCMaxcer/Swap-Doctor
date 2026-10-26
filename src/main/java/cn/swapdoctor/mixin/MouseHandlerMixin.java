package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import cn.swapdoctor.core.ActionButton;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public final class MouseHandlerMixin {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void swapdoctor$recordMousePress(long window, MouseButtonInfo button, int action, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS) return;
        ActionButton mapped = switch (button.button()) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> ActionButton.LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> ActionButton.RIGHT;
            default -> null;
        };
        if (mapped != null) SwapDoctorClient.TRACKER.recordMousePress(mapped,
                SwapDoctorClient.TRACKER.tick(), System.nanoTime());
    }

    @Inject(method = "onScroll", at = @At("HEAD"))
    private void swapdoctor$recordHotbarScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        SwapDoctorClient.TRACKER.recordHotbarScroll(vertical,
                SwapDoctorClient.TRACKER.tick(), System.nanoTime());
    }
}
