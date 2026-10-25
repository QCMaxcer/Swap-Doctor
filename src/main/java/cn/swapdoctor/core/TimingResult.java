package cn.swapdoctor.core;
public record TimingResult(Status status, double deltaMs, Direction direction, SwitchEvent switchEvent, ActionEvent actionEvent) {
 public enum Status { TIMING_PASS, TIMING_FAIL_CROSS_TICK }
 public enum Direction { SWITCH_THEN_ACTION, ACTION_THEN_SWITCH }
 public boolean passed(){ return status==Status.TIMING_PASS; }
}
