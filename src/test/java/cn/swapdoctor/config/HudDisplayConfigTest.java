package cn.swapdoctor.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class HudDisplayConfigTest {
    @Test
    void defaultsProvideIndependentHudModesAndLayout() {
        SwapDoctorConfig config = new SwapDoctorConfig();

        assertTrue(config.hudEnabled);
        assertTrue(config.scaleHudEnabled);
        assertEquals(50, config.maxDelayMs);
        assertEquals(1000, config.hudDurationMs);
        assertEquals(1000, config.scaleHudDurationMs);
        assertEquals(HudDisplayMode.INSTANT, config.hudMode);
        assertEquals(HudDisplayMode.PERSISTENT, config.scaleHudMode);
        assertEquals(120, config.scaleWidth);
        assertTrue(config.showScaleValues);
        assertEquals(0.5f, config.scaleLengthMultiplier, 0.0001f);
        assertEquals(0.5f, config.scaleThicknessMultiplier, 0.0001f);
        assertEquals(PositiveDirection.SWITCH_THEN_ACTION, config.positiveDirection);
    }

    @Test
    void roundTripPersistsNewHudSettings() throws Exception {
        Path path = Files.createTempDirectory("swapdoctor-config").resolve("swapdoctor.json");
        ConfigStore store = new ConfigStore(path);
        SwapDoctorConfig config = new SwapDoctorConfig();
        config.hudMode = HudDisplayMode.PERSISTENT;
        config.scaleHudMode = HudDisplayMode.INSTANT;
        config.scaleHudEnabled = false;
        config.maxDelayMs = 450;
        config.scaleHudDurationMs = 2750;
        config.scaleWidth = 244;
        config.scaleLengthMultiplier = 0.65f;
        config.scaleThicknessMultiplier = 0.85f;
        config.showScaleValues = false;
        config.hudX = 0.25f;
        config.hudY = 0.4f;
        config.scaleX = 0.6f;
        config.scaleY = 0.15f;
        config.positiveDirection = PositiveDirection.ACTION_THEN_SWITCH;

        store.save(config);
        SwapDoctorConfig loaded = store.load();

        assertEquals(HudDisplayMode.PERSISTENT, loaded.hudMode);
        assertEquals(HudDisplayMode.INSTANT, loaded.scaleHudMode);
        assertTrue(!loaded.scaleHudEnabled);
        assertEquals(450, loaded.maxDelayMs);
        assertEquals(2750, loaded.scaleHudDurationMs);
        assertEquals(244, loaded.scaleWidth);
        assertEquals(0.65f, loaded.scaleLengthMultiplier, 0.0001f);
        assertEquals(0.85f, loaded.scaleThicknessMultiplier, 0.0001f);
        assertTrue(!loaded.showScaleValues);
        assertEquals(0.25f, loaded.hudX, 0.0001f);
        assertEquals(0.4f, loaded.hudY, 0.0001f);
        assertEquals(0.6f, loaded.scaleX, 0.0001f);
        assertEquals(0.15f, loaded.scaleY, 0.0001f);
        assertEquals(PositiveDirection.ACTION_THEN_SWITCH, loaded.positiveDirection);
    }

    @Test
    void legacyConfigGetsNewDefaults() throws Exception {
        Path path = Files.createTempFile("swapdoctor-legacy", ".json");
        Files.writeString(path, "{\"maxDelayMs\":100,\"hudEnabled\":false,\"hudDurationMs\":1500,\"showDecimals\":true}");

        SwapDoctorConfig loaded = new ConfigStore(path).load();

        assertTrue(!loaded.hudEnabled);
        assertTrue(loaded.scaleHudEnabled);
        assertEquals(HudDisplayMode.INSTANT, loaded.hudMode);
        assertEquals(HudDisplayMode.PERSISTENT, loaded.scaleHudMode);
        assertEquals(120, loaded.scaleWidth);
        assertEquals(0.5f, loaded.scaleLengthMultiplier, 0.0001f);
        assertEquals(0.5f, loaded.scaleThicknessMultiplier, 0.0001f);
        assertTrue(loaded.showScaleValues);
        assertEquals(PositiveDirection.SWITCH_THEN_ACTION, loaded.positiveDirection);
    }

    @Test
    void layoutAndWidthValuesAreClamped() {
        SwapDoctorConfig config = new SwapDoctorConfig();
        config.maxDelayMs = 9999;
        config.hudDurationMs = 9999;
        config.scaleWidth = 9999;
        config.scaleHudDurationMs = 0;
        config.hudX = -1.0f;
        config.hudY = 2.0f;
        config.scaleX = Float.NaN;
        config.scaleY = Float.POSITIVE_INFINITY;

        config.validate();

        assertEquals(500, config.maxDelayMs);
        assertEquals(3000, config.hudDurationMs);
        assertEquals(300, config.scaleWidth);
        assertEquals(1, config.scaleHudDurationMs);
        assertEquals(0.0f, config.hudX);
        assertEquals(1.0f, config.hudY);
        assertEquals(0.0f, config.scaleX);
        assertEquals(0.0f, config.scaleY);

        config.scaleWidth = 299;
        config.validate();
        assertEquals(300, config.scaleWidth);
    }

    @Test
    void styleMultiplierValuesAreClampedSnappedAndDefaulted() {
        SwapDoctorConfig config = new SwapDoctorConfig();
        config.scaleLengthMultiplier = 0.13f;
        config.scaleThicknessMultiplier = 0.14f;
        config.validate();
        assertEquals(0.15f, config.scaleLengthMultiplier, 0.0001f);
        assertEquals(0.15f, config.scaleThicknessMultiplier, 0.0001f);

        config.scaleLengthMultiplier = -2.0f;
        config.scaleThicknessMultiplier = 1.8f;
        config.validate();
        assertEquals(0.1f, config.scaleLengthMultiplier, 0.0001f);
        assertEquals(1.0f, config.scaleThicknessMultiplier, 0.0001f);

        config.scaleLengthMultiplier = Float.NaN;
        config.scaleThicknessMultiplier = Float.POSITIVE_INFINITY;
        config.validate();
        assertEquals(0.5f, config.scaleLengthMultiplier, 0.0001f);
        assertEquals(0.5f, config.scaleThicknessMultiplier, 0.0001f);
    }
}
