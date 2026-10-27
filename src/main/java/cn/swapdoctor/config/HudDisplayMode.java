package cn.swapdoctor.config;

public enum HudDisplayMode {
    PERSISTENT("persistent"),
    INSTANT("instant");

    private final String serialized;

    HudDisplayMode(String serialized) {
        this.serialized = serialized;
    }

    public String serialized() {
        return serialized;
    }

    public static HudDisplayMode fromSerialized(String value) {
        for (HudDisplayMode mode : values()) {
            if (mode.serialized.equals(value)) return mode;
        }
        throw new IllegalArgumentException("Unknown HUD mode: " + value);
    }
}
