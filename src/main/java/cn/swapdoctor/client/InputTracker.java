package cn.swapdoctor.client;
import cn.swapdoctor.config.*; import cn.swapdoctor.core.*; import java.util.*; import net.minecraft.client.Minecraft;
public final class InputTracker {
 public static final InputTracker INSTANCE=new InputTracker(); private final InputGate gate=new InputGate(); private SwapMatcher matcher; private Optional<TimingResult> latest=Optional.empty(); private long tick;
 public InputTracker(){ matcher=new SwapMatcher(new ConfigStore().load().maxDelayMs); }
 public InputGate gate(){return gate;} public long tick(){return tick;} public void beginTick(){tick++; refreshContext();} public void endTick(){matcher.expire(System.nanoTime());}
 private void refreshContext(){ try { Minecraft mc=Minecraft.getInstance(); gate.setGameplay(mc.player!=null&&mc.level!=null); gate.setScreen(mc.screen!=null); gate.setPaused(mc.isPaused()); gate.setFocused(mc.isWindowActive()); } catch (Throwable ignored) {} }
 public void recordMousePress(ActionButton b,long t,long n){refreshContext(); if(!gate.allow())return; matcher.onAction(new ActionEvent(n,t,b)).ifPresent(x->latest=Optional.of(x));}
 public void recordHotbarSwitch(int from,int to,long t,long n){refreshContext(); if(!gate.allow()||from==to)return; matcher.onSwitch(new SwitchEvent(n,t,SwapKind.HOTBAR)).ifPresent(x->latest=Optional.of(x));}
 public void recordHandSwap(long t,long n){refreshContext(); if(!gate.allow())return; matcher.onSwitch(new SwitchEvent(n,t,SwapKind.HAND_SWAP)).ifPresent(x->latest=Optional.of(x));}
 public void clearState(){matcher.reset(); latest=Optional.empty(); gate.clear();} public Optional<TimingResult> latestResult(){return latest;} public void setThreshold(int ms){matcher=new SwapMatcher(ms); latest=Optional.empty();}
}
