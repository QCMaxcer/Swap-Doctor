package cn.swapdoctor.core;
import org.junit.jupiter.api.*;import static org.junit.jupiter.api.Assertions.*;
class SwapMatcherTest {
 @Test void switchBeforeActionPasses(){var m=new SwapMatcher(100);var r=m.onSwitch(new SwitchEvent(1_000_000,4));var result=m.onAction(new ActionEvent(51_000_000,4)).orElseThrow();assertTrue(result.passed());assertEquals(50.0,result.deltaMs(),0.01);}
 @Test void actionBeforeSwitchSignedAndDirection(){var m=new SwapMatcher(100);m.onAction(new ActionEvent(50_000_000,2));var r=m.onSwitch(new SwitchEvent(100_000_000,2)).orElseThrow();assertEquals(-50,r.deltaMs(),.01);assertEquals(TimingResult.Direction.ACTION_THEN_SWITCH,r.direction());}
 @Test void crossTickFailsAndOverWindowSilent(){var m=new SwapMatcher(100);m.onSwitch(new SwitchEvent(0,1));assertEquals(TimingResult.Status.TIMING_FAIL_CROSS_TICK,m.onAction(new ActionEvent(50_000_000,2)).orElseThrow().status());assertTrue(m.onAction(new ActionEvent(1_000_000_000,1)).isEmpty());}
 @Test void resetAndExpire(){var m=new SwapMatcher(100);m.onSwitch(new SwitchEvent(0,1));m.reset();assertTrue(m.onAction(new ActionEvent(1,1)).isEmpty());m.expire(200_000_000);assertTrue(m.onSwitch(new SwitchEvent(0,1)).isEmpty());}
}


