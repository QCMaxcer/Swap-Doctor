package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.swapdoctor.config.HudDisplayMode;
import cn.swapdoctor.config.PositiveDirection;
import cn.swapdoctor.core.TimingResult;
import org.junit.jupiter.api.Test;

class SwapDoctorHudMathTest {
    @Test
    void displayDeltaHonorsConfiguredDirection() {
        assertEquals(12.5, SwapDoctorHud.displayDelta(12.5, PositiveDirection.SWITCH_THEN_ACTION));
        assertEquals(-12.5, SwapDoctorHud.displayDelta(12.5, PositiveDirection.ACTION_THEN_SWITCH));
    }

    @Test
    void scalePointerClampsToConfiguredRange() {
        assertEquals(-140.0f, SwapDoctorScaleHud.pointerOffset(-200.0, 100, 280), 0.0001f);
        assertEquals(140.0f, SwapDoctorScaleHud.pointerOffset(200.0, 100, 280), 0.0001f);
        assertEquals(-70.0f, SwapDoctorScaleHud.pointerOffset(-50.0, 100, 280), 0.0001f);
    }

    @Test
    void instantAlphaFadesAndPersistentDoesNot() {
        assertEquals(255, SwapDoctorHud.alphaFor(HudDisplayMode.INSTANT, 0, 1000));
        assertEquals(127, SwapDoctorHud.alphaFor(HudDisplayMode.INSTANT, 500, 1000));
        assertEquals(0, SwapDoctorHud.alphaFor(HudDisplayMode.INSTANT, 1000, 1000));
        assertEquals(255, SwapDoctorHud.alphaFor(HudDisplayMode.PERSISTENT, 5000, 1000));
    }

    @Test
    void colorsRepresentNeutralPassAndFailure() {
        assertTrue((SwapDoctorHud.colorFor(null) >>> 24) == 0xFF);
        assertTrue((SwapDoctorHud.colorFor(TimingResult.Status.TIMING_PASS) >>> 8 & 0xFF) > 0xA0);
        assertTrue((SwapDoctorHud.colorFor(TimingResult.Status.TIMING_FAIL_CROSS_TICK) >>> 16 & 0xFF) > 0xA0);
    }

    @Test
    void scaleUsesOneMinorTickBetweenEachMajorTick() {
        assertEquals(9, SwapDoctorScaleHud.TICK_COUNT);
        for (int index = 0; index < SwapDoctorScaleHud.TICK_COUNT; index++) {
            assertEquals(index % 2 == 0, SwapDoctorScaleHud.isMajorTick(index));
        }
    }

    @Test
    void scalePointerAndGeometryRemainStableWhenLabelsAreOptional() {
        assertEquals(140.0f, SwapDoctorScaleHud.pointerOffset(100.0, 100, 280), 0.0001f);
    }

    @Test
    void pixelTickStyleKeepsTallMajorAndShortMinorMarks() {
        assertEquals(8, SwapDoctorScaleHud.tickHeight(0));
        assertEquals(4, SwapDoctorScaleHud.tickHeight(1));
    }

    @Test
    void tickBottomExtensionMatchesReferenceStyle() {
        assertEquals(8, SwapDoctorScaleHud.tickBottomExtension(0));
        assertEquals(4, SwapDoctorScaleHud.tickBottomExtension(1));
    }

    @Test
    void pointerStyleUsesThinVerticalGeometry() {
        assertEquals(2, SwapDoctorScaleHud.pointerWidth(0.75f));
        assertEquals(12, SwapDoctorScaleHud.pointerTopOffset(1.0f));
        assertEquals(12, SwapDoctorScaleHud.pointerBottomOffset(1.0f));
    }

    @Test
    void renderGeometryUsesEvenlySpacedTicksAndReferenceSpans() {
        int left = 10;
        int right = 90;
        int baseline = 40;
        for (int index = 0; index < SwapDoctorScaleHud.TICK_COUNT; index++) {
            assertEquals(left + index * 10, SwapDoctorScaleHud.tickX(index, left, right));
        }
        assertEquals(baseline - 8, SwapDoctorScaleHud.tickTop(baseline, 0, 1.0f));
        assertEquals(baseline + 9, SwapDoctorScaleHud.tickBottomExclusive(baseline, 0, 1.0f));
        assertEquals(baseline - 4, SwapDoctorScaleHud.tickTop(baseline, 1, 1.0f));
        assertEquals(baseline + 5, SwapDoctorScaleHud.tickBottomExclusive(baseline, 1, 1.0f));
    }

    @Test
    void pointerGeometryKeepsThreePixelWidthAndSymmetricHeight() {
        assertEquals(9, SwapDoctorScaleHud.pointerLeft(10, 0.75f));
        assertEquals(28, SwapDoctorScaleHud.pointerTop(40, 1.0f));
        assertEquals(53, SwapDoctorScaleHud.pointerBottomExclusive(40, 1.0f));
    }

    @Test
    void symmetricGeometryCentersTicksAndPointer() {
        assertEquals(8, SwapDoctorScaleHud.tickTopOffset(0, 1.0f));
        assertEquals(8, SwapDoctorScaleHud.tickBottomOffset(0, 1.0f));
        assertEquals(4, SwapDoctorScaleHud.tickTopOffset(1, 1.0f));
        assertEquals(4, SwapDoctorScaleHud.tickBottomOffset(1, 1.0f));
        assertEquals(21, SwapDoctorScaleHud.baselineY(0, 1.0f));
    }

    @Test
    void scaleGeometryHonorsLengthAndThicknessMultipliers() {
        assertEquals(16, SwapDoctorScaleHud.tickTopOffset(0, 2.0f));
        assertEquals(4, SwapDoctorScaleHud.tickTopOffset(0, 0.5f));
        assertEquals(24, SwapDoctorScaleHud.pointerTopOffset(2.0f));
        assertEquals(6, SwapDoctorScaleHud.pointerTopOffset(0.5f));
        assertEquals(1, SwapDoctorScaleHud.strokeWidth(0.5f));
        assertEquals(2, SwapDoctorScaleHud.strokeWidth(2.0f));
        assertEquals(6, SwapDoctorScaleHud.pointerWidth(2.0f));
    }

    @Test
    void scaleLineBoundsMatchLabelVisibilityAndWidths() {
        assertEquals(28, SwapDoctorScaleHud.scaleLineLeft(10, 280, 10, true));
        assertEquals(272, SwapDoctorScaleHud.scaleLineRight(10, 280, 10, true));
        assertEquals(14, SwapDoctorScaleHud.scaleLineLeft(10, 280, 10, false));
        assertEquals(286, SwapDoctorScaleHud.scaleLineRight(10, 280, 10, false));
    }

}
