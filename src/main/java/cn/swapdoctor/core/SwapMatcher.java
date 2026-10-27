package cn.swapdoctor.core;
import java.util.*;
public final class SwapMatcher {
 private final long maxDelayNanos;
 private final List<SwitchEvent> switches=new ArrayList<>();
 private final List<ActionEvent> actions=new ArrayList<>();
 public SwapMatcher(){this(50);}
 public SwapMatcher(long maxDelayMs){ if(maxDelayMs<1||maxDelayMs>500) throw new IllegalArgumentException("maxDelayMs"); this.maxDelayNanos=maxDelayMs*1_000_000L; }
 public Optional<TimingResult> onSwitch(SwitchEvent e){ Objects.requireNonNull(e); ActionEvent a=nearestAction(e.nanoTime()); if(a==null){switches.add(e); return Optional.empty();} actions.remove(a); return Optional.of(result(e,a)); }
 public Optional<TimingResult> onAction(ActionEvent e){ Objects.requireNonNull(e); SwitchEvent s=nearestSwitch(e.nanoTime()); if(s==null){actions.add(e); return Optional.empty();} switches.remove(s); return Optional.of(result(s,e)); }
 private ActionEvent nearestAction(long t){ ActionEvent best=null; long d=Long.MAX_VALUE; for(ActionEvent e:actions){long x=Math.abs(e.nanoTime()-t); if(x<=maxDelayNanos&&x<d){best=e;d=x;}} return best; }
 private SwitchEvent nearestSwitch(long t){ SwitchEvent best=null; long d=Long.MAX_VALUE; for(SwitchEvent e:switches){long x=Math.abs(e.nanoTime()-t); if(x<=maxDelayNanos&&x<d){best=e;d=x;}} return best; }
 private TimingResult result(SwitchEvent s,ActionEvent a){double delta=(a.nanoTime()-s.nanoTime())/1_000_000.0; return new TimingResult(s.tick()==a.tick()?TimingResult.Status.TIMING_PASS:TimingResult.Status.TIMING_FAIL_CROSS_TICK,delta,delta>=0?TimingResult.Direction.SWITCH_THEN_ACTION:TimingResult.Direction.ACTION_THEN_SWITCH,s,a);}
 public void expire(long nowNanos){switches.removeIf(e->nowNanos-e.nanoTime()>maxDelayNanos); actions.removeIf(e->nowNanos-e.nanoTime()>maxDelayNanos);}
 public void reset(){switches.clear();actions.clear();}
}
