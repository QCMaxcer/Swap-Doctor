package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.swapdoctor.core.TimingResult;
import org.junit.jupiter.api.Test;

class SwapDoctorHudTest {
    @Test
    void formatsSignedMillisecondsWithThreeDecimals() {
        assertEquals("+12.340", SwapDoctorHud.formatDelta(12.34));
        assertEquals("-7.000", SwapDoctorHud.formatDelta(-7.0));
        assertEquals("+0.004", SwapDoctorHud.formatDelta(0.004));
    }

    @Test
    void hudColorsAreOpaqueArgbValues() {
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_PASS) >>> 24) & 0xFF);
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_FAIL_CROSS_TICK) >>> 24) & 0xFF);
    }
}
