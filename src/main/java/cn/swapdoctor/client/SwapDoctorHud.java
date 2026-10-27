package cn.swapdoctor.client;

import cn.swapdoctor.config.HudDisplayMode;
import cn.swapdoctor.config.PositiveDirection;
import cn.swapdoctor.core.TimingHudSnapshot;
import cn.swapdoctor.core.TimingResult;
import java.util.Locale;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class SwapDoctorHud implements HudElement {
    private static final Identifier ID = Identifier.fromNamespaceAndPath("swapdoctor", "timing_hud");
    private static final int PASS_COLOR = 0xFF55FF72;
    private static final int FAIL_COLOR = 0xFFFF5555;
    private static final int NEUTRAL_COLOR = 0xFFC4C8D0;

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, ID, new SwapDoctorHud());
    }

    @Override
    public void render(GuiGraphics context, DeltaTracker tickCounter) {
        var config = SwapDoctorClient.config();
        if (!config.hudEnabled) return;

        TimingHudSnapshot snapshot = SwapDoctorClient.TRACKER.latestSnapshot().orElse(null);
        long ageMs = snapshot == null ? 0L : Math.max(0L, (System.nanoTime() - snapshot.receivedAtNanos()) / 1_000_000L);
        int alpha = alphaFor(config.hudMode, ageMs, config.hudDurationMs);
        if (snapshot == null && config.hudMode == HudDisplayMode.INSTANT) return;
        if (alpha <= 0) return;

        Minecraft client = Minecraft.getInstance();
        String text;
        int color;
        if (snapshot == null) {
            color = NEUTRAL_COLOR;
            int x = HudLayout.screenX(config.hudPosition(), client.getWindow().getGuiScaledWidth());
            int y = HudLayout.screenY(config.hudPosition(), client.getWindow().getGuiScaledHeight());
            context.drawString(client.font, Component.translatable("swapdoctor.hud.waiting"), x, y,
                    withAlpha(color, alpha), true);
            return;
        } else {
            TimingResult result = snapshot.result();
            double delta = displayDelta(result.deltaMs(), config.positiveDirection);
            text = formatDelta(delta, config.showDecimals);
            color = colorFor(result.status());
        }
        int x = HudLayout.screenX(config.hudPosition(), client.getWindow().getGuiScaledWidth());
        int y = HudLayout.screenY(config.hudPosition(), client.getWindow().getGuiScaledHeight());
        context.drawString(client.font, text, x, y, withAlpha(color, alpha), true);
    }

    static double displayDelta(double rawDeltaMs, PositiveDirection direction) {
        return direction == PositiveDirection.ACTION_THEN_SWITCH ? -rawDeltaMs : rawDeltaMs;
    }

    static int alphaFor(HudDisplayMode mode, long ageMs, int durationMs) {
        if (mode == HudDisplayMode.PERSISTENT) return 255;
        if (durationMs <= 0 || ageMs >= durationMs) return 0;
        return Math.max(0, Math.min(255, (int) (255.0 * (durationMs - ageMs) / durationMs)));
    }

    static int colorFor(TimingResult.Status status) {
        if (status == null) return NEUTRAL_COLOR;
        return status == TimingResult.Status.TIMING_PASS ? PASS_COLOR : FAIL_COLOR;
    }

    static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (Math.max(0, Math.min(255, alpha)) << 24);
    }

    static String formatDelta(double deltaMs, boolean showDecimals) {
        if (showDecimals) return String.format(Locale.ROOT, "%+.3f ms", deltaMs);
        long rounded = Math.round(Math.abs(deltaMs));
        return (deltaMs < 0 ? "-" : "+") + rounded + " ms";
    }
}
