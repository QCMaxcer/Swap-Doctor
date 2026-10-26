package cn.swapdoctor.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class InputGateTest {
    @Test void allowsOnlyFocusedGameplayWithoutScreenOrPause() {
        InputGate gate = new InputGate();
        gate.setGameplay(true);
        assertTrue(gate.allow());
        gate.setScreenOpen(true);
        assertFalse(gate.allow());
        gate.setScreenOpen(false);
        gate.setPaused(true);
        assertFalse(gate.allow());
        gate.setPaused(false);
        gate.setFocused(false);
        assertFalse(gate.allow());
    }
    @Test void clearDisablesInput() {
        InputGate gate = new InputGate();
        gate.setGameplay(true);
        gate.clear();
        assertFalse(gate.allow());
    }
}
