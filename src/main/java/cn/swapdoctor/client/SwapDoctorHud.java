package cn.swapdoctor.client;

import cn.swapdoctor.core.TimingResult;
import java.util.Locale;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

/** Renders the most recent local timing result without changing matcher state. */
public final class SwapDoctorHud implements HudElement {
    private static final Identifier ID = Identifier.fromNamespaceAndPath("swapdoctor", "timing_hud");
    private static final int PASS_COLOR = 0xFF55FF55;
    private static final int FAIL_COLOR = 0xFFFF5555;

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, ID, new SwapDoctorHud());
    }

    @Override
    public void render(GuiGraphics context, DeltaTracker tickCounter) {
        var config = SwapDoctorClient.config();
        if (!config.hudEnabled) return;

        Optional<TimingResult> latest = SwapDoctorClient.TRACKER.latestResult();
        if (latest.isEmpty()) return;
        TimingResult result = latest.get();
        long age = System.nanoTime() - Math.max(result.switchEvent().nanoTime(), result.actionEvent().nanoTime());
        if (age < 0 || age > config.hudDurationMs * 1_000_000L) return;

        Minecraft client = Minecraft.getInstance();
        context.drawString(client.font, formatDelta(result.deltaMs()), 8, 8,
                colorFor(result.status()), true);
    }

    static int colorFor(TimingResult.Status status) {
        return status == TimingResult.Status.TIMING_PASS ? PASS_COLOR : FAIL_COLOR;
    }

    static String formatDelta(double deltaMs) {
        return String.format(Locale.ROOT, "%+.3f ms", deltaMs);
    }
}
