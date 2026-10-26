package cn.swapdoctor.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ActionEventTest {
    @Test
    void legacyButtonConstructorInfersGameplayAction() {
        assertEquals(ActionKind.ATTACK, new ActionEvent(1, 2, ActionButton.LEFT).kind());
        assertEquals(ActionKind.USE, new ActionEvent(1, 2, ActionButton.RIGHT).kind());
    }
}
