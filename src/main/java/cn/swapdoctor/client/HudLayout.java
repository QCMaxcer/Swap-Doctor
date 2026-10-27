package cn.swapdoctor.client;

import cn.swapdoctor.config.HudPosition;

final class HudLayout {
    private HudLayout() {
    }

    static int screenX(HudPosition position, int screenWidth) {
        return Math.round(position.x() * screenWidth);
    }

    static int screenY(HudPosition position, int screenHeight) {
        return Math.round(position.y() * screenHeight);
    }

    static float clampX(float value, int hudWidth, int screenWidth) {
        if (screenWidth <= hudWidth) return 0.0f;
        return clamp(value, 0.0f, (screenWidth - hudWidth) / (float) screenWidth);
    }

    static float clampY(float value, int hudHeight, int screenHeight) {
        if (screenHeight <= hudHeight) return 0.0f;
        return clamp(value, 0.0f, (screenHeight - hudHeight) / (float) screenHeight);
    }

    private static float clamp(float value, float min, float max) {
        if (!Float.isFinite(value)) return min;
        return Math.max(min, Math.min(max, value));
    }
}
