package cn.swapdoctor.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.swapdoctor.config.HudPosition;
import org.junit.jupiter.api.Test;

class HudLayoutTest {
    @Test
    void normalizedPositionConvertsAndClampsToScreen() {
        HudPosition position = new HudPosition(0.5f, 0.25f);
        assertEquals(960, HudLayout.screenX(position, 1920));
        assertEquals(270, HudLayout.screenY(position, 1080));
        assertEquals(0.0f, HudLayout.clampX(-0.2f, 100, 1920), 0.0001f);
        assertEquals(0.75f, HudLayout.clampY(2.0f, 200, 800), 0.0001f);
    }
}
