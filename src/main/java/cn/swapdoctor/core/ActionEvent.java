package cn.swapdoctor.core;
public record ActionEvent(long nanoTime, long tick, ActionButton button) {
 public ActionEvent(long nanoTime,long tick){this(nanoTime,tick,ActionButton.LEFT);}
}
