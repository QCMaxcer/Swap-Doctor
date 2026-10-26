package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.OptionalLong;
import org.junit.jupiter.api.Test;

class PendingSwitchInputsTest {
    @Test
    void consumesTheNearestKeyboardTimestampWithinOneTick() {
        PendingSwitchInputs pending = new PendingSwitchInputs();
        pending.add(10, 100L);
        assertEquals(OptionalLong.of(100L), pending.consume(11));
    }

    @Test
    void rejectsOlderKeyboardTimestamps() {
        PendingSwitchInputs pending = new PendingSwitchInputs();
        pending.add(10, 100L);
        pending.discardBefore(12);
        assertTrue(pending.consume(12).isEmpty());
    }
}
