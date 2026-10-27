package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.swapdoctor.core.TimingResult;
import org.junit.jupiter.api.Test;

class SwapDoctorHudTest {
    @Test
    void formatsSignedMillisecondsWithThreeDecimals() {
        assertEquals("+12.340 ms", SwapDoctorHud.formatDelta(12.34, true));
        assertEquals("-7.000 ms", SwapDoctorHud.formatDelta(-7.0, true));
        assertEquals("+0.004 ms", SwapDoctorHud.formatDelta(0.004, true));
    }

    @Test
    void formatsSignedMillisecondsAsIntegerByDefault() {
        assertEquals("+12 ms", SwapDoctorHud.formatDelta(12.34, false));
        assertEquals("-7 ms", SwapDoctorHud.formatDelta(-7.0, false));
        assertEquals("-0 ms", SwapDoctorHud.formatDelta(-0.004, false));
    }

    @Test
    void hudColorsAreOpaqueArgbValues() {
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_PASS) >>> 24) & 0xFF);
        assertEquals(0xFF, (SwapDoctorHud.colorFor(TimingResult.Status.TIMING_FAIL_CROSS_TICK) >>> 24) & 0xFF);
    }
}
