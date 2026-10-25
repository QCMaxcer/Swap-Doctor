package cn.swapdoctor.core;
public record SwitchEvent(long nanoTime, long tick, SwapKind kind) {
 public SwitchEvent(long nanoTime,long tick){this(nanoTime,tick,SwapKind.HOTBAR);}
}
