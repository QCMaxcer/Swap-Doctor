package cn.swapdoctor.client;

import cn.swapdoctor.config.HudDisplayMode;
import cn.swapdoctor.core.TimingHudSnapshot;
import cn.swapdoctor.core.TimingResult;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

public final class SwapDoctorScaleHud implements HudElement {
    private static final Identifier ID = Identifier.fromNamespaceAndPath("swapdoctor", "scale_hud");
    static final int HEIGHT = 42;
    static final int TICK_COUNT = 9;
    private static final int BASE_MAJOR_TICK_LENGTH = 8;
    private static final int BASE_MINOR_TICK_LENGTH = 4;
    private static final int BASE_POINTER_LENGTH = 12;
    private static final int BASE_STROKE_WIDTH = 1;
    private static final int BASE_POINTER_WIDTH = 3;
    static final int SCALE_COLOR = 0xFFC7CED6;

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, ID, new SwapDoctorScaleHud());
    }

    @Override
    public void render(GuiGraphics context, DeltaTracker tickCounter) {
        var config = SwapDoctorClient.config();
        if (!config.scaleHudEnabled) return;

        TimingHudSnapshot snapshot = SwapDoctorClient.TRACKER.latestSnapshot().orElse(null);
        long ageMs = snapshot == null ? 0L : Math.max(0L, (System.nanoTime() - snapshot.receivedAtNanos()) / 1_000_000L);
        int alpha = SwapDoctorHud.alphaFor(config.scaleHudMode, ageMs, config.scaleHudDurationMs);
        if (snapshot == null && config.scaleHudMode == HudDisplayMode.INSTANT) return;
        if (alpha <= 0) return;

        Minecraft client = Minecraft.getInstance();
        int width = config.scaleWidth;
        int x = HudLayout.screenX(config.scalePosition(), client.getWindow().getGuiScaledWidth());
        int y = HudLayout.screenY(config.scalePosition(), client.getWindow().getGuiScaledHeight());
        float lengthMultiplier = config.scaleLengthMultiplier;
        float thicknessMultiplier = config.scaleThicknessMultiplier;
        String minLabel = formatScaleValue(-config.maxDelayMs);
        String maxLabel = formatScaleValue(config.maxDelayMs);
        int baseline = baselineY(y, lengthMultiplier);
        int strokeWidth = strokeWidth(thicknessMultiplier);
        int labelY = baseline - client.font.lineHeight / 2;
        int minLabelWidth = client.font.width(minLabel);
        int maxLabelWidth = client.font.width(maxLabel);
        int left = scaleLineLeft(x, width, minLabelWidth, config.showScaleValues);
        int right = scaleLineRight(x, width, maxLabelWidth, config.showScaleValues);
        if (config.showScaleValues) {
            int labelColor = SwapDoctorHud.withAlpha(SCALE_COLOR, alpha);
            context.drawString(client.font, minLabel, x, labelY, labelColor, false);
            context.drawString(client.font, maxLabel, x + width - maxLabelWidth, labelY, labelColor, false);
        }
        int center = (left + right) / 2;
        int baselineTop = baseline - strokeWidth / 2;
        context.fill(left, baselineTop, right + 1, baselineTop + strokeWidth,
                SwapDoctorHud.withAlpha(SCALE_COLOR, alpha));
        for (int i = 0; i < TICK_COUNT; i++) {
            int tickX = tickX(i, left, right);
            context.fill(tickX, tickTop(baseline, i, lengthMultiplier), tickX + strokeWidth,
                    tickBottomExclusive(baseline, i, lengthMultiplier),
                    SwapDoctorHud.withAlpha(SCALE_COLOR, alpha));
        }

        double displayDelta = snapshot == null
                ? 0.0
                : SwapDoctorHud.displayDelta(snapshot.result().deltaMs(), config.positiveDirection);
        float offset = pointerOffset(displayDelta, config.maxDelayMs, right - left);
        int pointerX = center + Math.round(offset);
        int pointerColor = SwapDoctorHud.colorFor(snapshot == null ? null : snapshot.result().status());
        pointerColor = SwapDoctorHud.withAlpha(pointerColor, alpha);
        int pointerWidth = pointerWidth(thicknessMultiplier);
        int pointerStart = pointerLeft(pointerX, thicknessMultiplier);
        context.fill(pointerStart, pointerTop(baseline, lengthMultiplier), pointerStart + pointerWidth,
                pointerBottomExclusive(baseline, lengthMultiplier), pointerColor);
    }

    static boolean isMajorTick(int index) {
        return index >= 0 && index < TICK_COUNT && index % 2 == 0;
    }

    static int tickHeight(int index) {
        return tickTopOffset(index, 1.0f);
    }

    static int tickBottomExtension(int index) {
        return tickBottomOffset(index, 1.0f);
    }

    static int tickTopOffset(int index, float lengthMultiplier) {
        return scaleLength(isMajorTick(index) ? BASE_MAJOR_TICK_LENGTH : BASE_MINOR_TICK_LENGTH, lengthMultiplier);
    }

    static int tickBottomOffset(int index, float lengthMultiplier) {
        return scaleLength(isMajorTick(index) ? BASE_MAJOR_TICK_LENGTH : BASE_MINOR_TICK_LENGTH, lengthMultiplier);
    }

    static int scaleLineLeft(int x, int width, int labelWidth, boolean showScaleValues) {
        return showScaleValues ? x + labelWidth + 8 : x + 4;
    }

    static int scaleLineRight(int x, int width, int labelWidth, boolean showScaleValues) {
        return showScaleValues ? x + width - labelWidth - 8 : x + width - 4;
    }

    static int tickX(int index, int left, int right) {
        return left + Math.round((right - left) * index / (float) (TICK_COUNT - 1));
    }

    static int tickTop(int baseline, int index, float lengthMultiplier) {
        return baseline - tickTopOffset(index, lengthMultiplier);
    }

    static int tickBottomExclusive(int baseline, int index, float lengthMultiplier) {
        return baseline + tickBottomOffset(index, lengthMultiplier) + 1;
    }

    static int pointerWidth(float thicknessMultiplier) {
        return Math.max(1, Math.round(BASE_POINTER_WIDTH * thicknessMultiplier));
    }

    static int pointerLeft(int pointerX, float thicknessMultiplier) {
        return pointerX - pointerWidth(thicknessMultiplier) / 2;
    }

    static int pointerTopOffset(float lengthMultiplier) {
        return scaleLength(BASE_POINTER_LENGTH, lengthMultiplier);
    }

    static int pointerBottomOffset(float lengthMultiplier) {
        return scaleLength(BASE_POINTER_LENGTH, lengthMultiplier);
    }

    static int pointerTop(int baseline, float lengthMultiplier) {
        return baseline - pointerTopOffset(lengthMultiplier);
    }

    static int pointerBottomExclusive(int baseline, float lengthMultiplier) {
        return baseline + pointerBottomOffset(lengthMultiplier) + 1;
    }

    static int strokeWidth(float thicknessMultiplier) {
        return Math.max(1, Math.round(BASE_STROKE_WIDTH * thicknessMultiplier));
    }

    static int height(float lengthMultiplier) {
        return Math.max(HEIGHT, pointerTopOffset(lengthMultiplier) + pointerBottomOffset(lengthMultiplier) + 2);
    }

    static int baselineY(int y, float lengthMultiplier) {
        return y + height(lengthMultiplier) / 2;
    }

    private static int scaleLength(int base, float multiplier) {
        return Math.max(1, Math.round(base * multiplier));
    }

    static float pointerOffset(double deltaMs, int maxDelayMs, int usableWidth) {
        if (maxDelayMs <= 0 || usableWidth <= 0) return 0.0f;
        double ratio = Math.max(-1.0, Math.min(1.0, deltaMs / maxDelayMs));
        return (float) (ratio * usableWidth / 2.0);
    }

    static String formatScaleValue(int value) {
        return value > 0 ? "+" + value : Integer.toString(value);
    }
}
