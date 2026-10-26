package cn.swapdoctor.core;
import org.junit.jupiter.api.*;import static org.junit.jupiter.api.Assertions.*;
class SwapMatcherBoundaryTest {
 @Test void inclusiveBoundariesAccepted(){for(long ms:new long[]{1,100,1000}) assertDoesNotThrow(()->new SwapMatcher(ms));}
 @Test void nearestPairConsumedOnce(){var m=new SwapMatcher(100);m.onSwitch(new SwitchEvent(0,1));m.onSwitch(new SwitchEvent(90_000_000,1));var r=m.onAction(new ActionEvent(95_000_000,1)).orElseThrow();assertEquals(5,r.deltaMs(),.01);assertEquals(96,m.onAction(new ActionEvent(96_000_000,1)).orElseThrow().deltaMs(),.01);assertTrue(m.onAction(new ActionEvent(97_000_000,1)).isEmpty());}
}

