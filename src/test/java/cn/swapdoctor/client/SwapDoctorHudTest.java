package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.swapdoctor.core.TimingResult;
import org.junit.jupiter.api.Test;

class SwapDoctorHudTest {
    @Test
    void formatsSignedMillisecondsWithOneDecimal() {
        assertEquals("+12.3", SwapDoctorHud.formatDelta(12.34));
        assertEquals("-7.0", SwapDoctorHud.formatDelta(-7.0));
        assertEquals("+0.0", SwapDoctorHud.formatDelta(0.0));
    }

    @Test
    void exposesDirectionTranslationKeys() {
        assertEquals("swapdoctor.direction.switch_first",
                SwapDoctorHud.directionKey(TimingResult.Direction.SWITCH_THEN_ACTION));
        assertEquals("swapdoctor.direction.click_first",
                SwapDoctorHud.directionKey(TimingResult.Direction.ACTION_THEN_SWITCH));
    }

    @Test
    void hudColorsAreOpaqueArgbValues() {
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_PASS) >>> 24) & 0xFF);
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_FAIL_CROSS_TICK) >>> 24) & 0xFF);
    }
}
