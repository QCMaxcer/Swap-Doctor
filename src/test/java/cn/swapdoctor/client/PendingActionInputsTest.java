package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.swapdoctor.core.ActionKind;
import java.util.OptionalLong;
import org.junit.jupiter.api.Test;

class PendingActionInputsTest {
    @Test
    void consumesOnlyMatchingActionFromTheSameTick() {
        PendingActionInputs pending = new PendingActionInputs();
        pending.add(ActionKind.ATTACK, 7, 100L);
        pending.add(ActionKind.USE, 7, 200L);

        assertEquals(OptionalLong.of(100L), pending.consume(ActionKind.ATTACK, 7));
        assertEquals(OptionalLong.of(200L), pending.consume(ActionKind.USE, 7));
        assertTrue(pending.consume(ActionKind.ATTACK, 7).isEmpty());
    }

    @Test
    void discardsInputsFromEarlierTicks() {
        PendingActionInputs pending = new PendingActionInputs();
        pending.add(ActionKind.ATTACK, 3, 100L);
        pending.discardBefore(5);
        assertTrue(pending.consume(ActionKind.ATTACK, 3).isEmpty());
    }

    @Test
    void allowsOneTickProcessingDelayWithoutLosingPhysicalTimestamp() {
        PendingActionInputs pending = new PendingActionInputs();
        pending.add(ActionKind.ATTACK, 7, 100L);
        assertEquals(OptionalLong.of(100L), pending.consume(ActionKind.ATTACK, 8));
    }
}
