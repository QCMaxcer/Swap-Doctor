package cn.swapdoctor.mixin;

import cn.swapdoctor.client.SwapDoctorClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public final class InventoryMixin {
    @Inject(method = "setSelectedSlot", at = @At("HEAD"))
    private void swapdoctor$recordSelectedSlot(int slot, CallbackInfo ci) {
        Inventory inventory = (Inventory) (Object) this;
        Minecraft client = Minecraft.getInstance();
        if (SwapDoctorClient.TRACKER.inInputContext() && inventory.player == client.player && client.screen == null) {
            SwapDoctorClient.TRACKER.recordHotbarSwitch(inventory.getSelectedSlot(), slot,
                    SwapDoctorClient.TRACKER.tick(), System.nanoTime());
        }
    }
}
