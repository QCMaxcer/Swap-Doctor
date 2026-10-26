package cn.swapdoctor.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/** Registers the fallback K shortcut so settings are available without Mod Menu. */
public final class SwapDoctorKeybinds {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("swapdoctor", "key_category"));
    private static KeyMapping openConfig;

    private SwapDoctorKeybinds() {
    }

    public static void register() {
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.swapdoctor.open_config", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register(SwapDoctorKeybinds::poll);
    }

    private static void poll(Minecraft client) {
        while (openConfig.consumeClick()) {
            client.setScreen(SwapDoctorConfigScreen.create(client.screen));
        }
    }
}
