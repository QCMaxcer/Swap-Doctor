package cn.swapdoctor.config;

public record HudPosition(float x, float y) {
    public HudPosition {
        x = clamp(x);
        y = clamp(y);
    }

    private static float clamp(float value) {
        if (!Float.isFinite(value)) return 0.0f;
        return Math.max(0.0f, Math.min(1.0f, value));
    }
}
