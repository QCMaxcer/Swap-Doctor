package cn.swapdoctor.core;

public record ActionEvent(long nanoTime, long tick, ActionButton button, ActionKind kind) {
    public ActionEvent(long nanoTime, long tick, ActionButton button) {
        this(nanoTime, tick, button, button == ActionButton.LEFT ? ActionKind.ATTACK : ActionKind.USE);
    }

    public ActionEvent(long nanoTime, long tick, ActionButton button, ActionKind kind) {
        this.nanoTime = nanoTime;
        this.tick = tick;
        this.button = button;
        this.kind = kind;
    }

    public ActionEvent(long nanoTime, long tick) {
        this(nanoTime, tick, ActionButton.LEFT, ActionKind.ATTACK);
    }
}
