package cn.swapdoctor.config;

public enum PositiveDirection {
    SWITCH_THEN_ACTION("switch_then_action"),
    ACTION_THEN_SWITCH("action_then_switch");

    private final String serialized;

    PositiveDirection(String serialized) {
        this.serialized = serialized;
    }

    public String serialized() {
        return serialized;
    }

    public static PositiveDirection fromSerialized(String value) {
        for (PositiveDirection direction : values()) {
            if (direction.serialized.equals(value)) return direction;
        }
        throw new IllegalArgumentException("Unknown positive direction: " + value);
    }
}
