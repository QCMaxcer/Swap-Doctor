package cn.swapdoctor.config;

public final class SwapDoctorConfig {
    public static final int DEFAULT_MAX_DELAY_MS = 50;
    public static final int DEFAULT_HUD_DURATION_MS = 1000;
    public static final int DEFAULT_SCALE_WIDTH = 120;
    public static final float DEFAULT_SCALE_LENGTH_MULTIPLIER = 0.5f;
    public static final float DEFAULT_SCALE_THICKNESS_MULTIPLIER = 0.5f;
    public static final float MIN_SCALE_STYLE_MULTIPLIER = 0.1f;
    public static final float MAX_SCALE_STYLE_MULTIPLIER = 1.0f;
    public static final float SCALE_STYLE_MULTIPLIER_STEP = 0.05f;

    public int maxDelayMs;
    public boolean hudEnabled;
    public int hudDurationMs;
    public boolean showDecimals;
    public boolean showScaleValues;
    public boolean scaleHudEnabled;
    public HudDisplayMode hudMode;
    public HudDisplayMode scaleHudMode;
    public int scaleHudDurationMs;
    public float hudX;
    public float hudY;
    public float scaleX;
    public float scaleY;
    public int scaleWidth;
    public float scaleLengthMultiplier;
    public float scaleThicknessMultiplier;
    public PositiveDirection positiveDirection;

    public SwapDoctorConfig() {
        maxDelayMs = DEFAULT_MAX_DELAY_MS;
        hudEnabled = true;
        hudDurationMs = DEFAULT_HUD_DURATION_MS;
        showDecimals = false;
        showScaleValues = true;
        scaleHudEnabled = true;
        hudMode = HudDisplayMode.INSTANT;
        scaleHudMode = HudDisplayMode.PERSISTENT;
        scaleHudDurationMs = DEFAULT_HUD_DURATION_MS;
        hudX = 0.02f;
        hudY = 0.02f;
        scaleX = 0.35f;
        scaleY = 0.10f;
        scaleWidth = DEFAULT_SCALE_WIDTH;
        scaleLengthMultiplier = DEFAULT_SCALE_LENGTH_MULTIPLIER;
        scaleThicknessMultiplier = DEFAULT_SCALE_THICKNESS_MULTIPLIER;
        positiveDirection = PositiveDirection.SWITCH_THEN_ACTION;
    }

    public SwapDoctorConfig(int maxDelayMs, boolean hudEnabled, int hudDurationMs) {
        this();
        this.maxDelayMs = maxDelayMs;
        this.hudEnabled = hudEnabled;
        this.hudDurationMs = hudDurationMs;
        validate();
    }

    public SwapDoctorConfig(int maxDelayMs, boolean hudEnabled, int hudDurationMs, boolean showDecimals) {
        this(maxDelayMs, hudEnabled, hudDurationMs);
        this.showDecimals = showDecimals;
    }

    public SwapDoctorConfig copy() {
        SwapDoctorConfig copy = new SwapDoctorConfig();
        copy.maxDelayMs = maxDelayMs;
        copy.hudEnabled = hudEnabled;
        copy.hudDurationMs = hudDurationMs;
        copy.showDecimals = showDecimals;
        copy.showScaleValues = showScaleValues;
        copy.scaleHudEnabled = scaleHudEnabled;
        copy.hudMode = hudMode;
        copy.scaleHudMode = scaleHudMode;
        copy.scaleHudDurationMs = scaleHudDurationMs;
        copy.hudX = hudX;
        copy.hudY = hudY;
        copy.scaleX = scaleX;
        copy.scaleY = scaleY;
        copy.scaleWidth = scaleWidth;
        copy.scaleLengthMultiplier = scaleLengthMultiplier;
        copy.scaleThicknessMultiplier = scaleThicknessMultiplier;
        copy.positiveDirection = positiveDirection;
        copy.validate();
        return copy;
    }

    public HudPosition hudPosition() {
        return new HudPosition(hudX, hudY);
    }

    public HudPosition scalePosition() {
        return new HudPosition(scaleX, scaleY);
    }

    public void setHudPosition(HudPosition position) {
        hudX = position.x();
        hudY = position.y();
    }

    public void setScalePosition(HudPosition position) {
        scaleX = position.x();
        scaleY = position.y();
    }

    public void validate() {
        maxDelayMs = clamp(maxDelayMs, 1, 500);
        hudDurationMs = clamp(hudDurationMs, 1, 3000);
        scaleHudDurationMs = clamp(scaleHudDurationMs, 1, 3000);
        scaleWidth = clamp(scaleWidth, 30, 300);
        scaleWidth = Math.round(scaleWidth / 2.0f) * 2;
        scaleLengthMultiplier = normalizeStyleMultiplier(scaleLengthMultiplier, DEFAULT_SCALE_LENGTH_MULTIPLIER);
        scaleThicknessMultiplier = normalizeStyleMultiplier(scaleThicknessMultiplier, DEFAULT_SCALE_THICKNESS_MULTIPLIER);
        hudX = clampUnit(hudX);
        hudY = clampUnit(hudY);
        scaleX = clampUnit(scaleX);
        scaleY = clampUnit(scaleY);
        if (hudMode == null) hudMode = HudDisplayMode.INSTANT;
        if (scaleHudMode == null) scaleHudMode = HudDisplayMode.PERSISTENT;
        if (positiveDirection == null) positiveDirection = PositiveDirection.SWITCH_THEN_ACTION;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clampUnit(float value) {
        if (!Float.isFinite(value)) return 0.0f;
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    private static float normalizeStyleMultiplier(float value, float fallback) {
        if (!Float.isFinite(value)) return fallback;
        float clamped = Math.max(MIN_SCALE_STYLE_MULTIPLIER, Math.min(MAX_SCALE_STYLE_MULTIPLIER, value));
        return Math.round(clamped / SCALE_STYLE_MULTIPLIER_STEP) * SCALE_STYLE_MULTIPLIER_STEP;
    }
}
