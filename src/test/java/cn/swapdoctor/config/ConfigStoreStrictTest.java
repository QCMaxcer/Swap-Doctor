package cn.swapdoctor.config;
import org.junit.jupiter.api.*;import java.nio.file.*;import static org.junit.jupiter.api.Assertions.*;
class ConfigStoreStrictTest {
 @Test void legacyThreeFieldConfigDefaultsDecimalsOff() throws Exception { Path p=Files.createTempFile("legacy", ".json"); Files.writeString(p,"{\"maxDelayMs\":321,\"hudEnabled\":true,\"hudDurationMs\":999}"); var x=new ConfigStore(p).load(); assertEquals(321,x.maxDelayMs); assertFalse(x.showDecimals); Files.deleteIfExists(p); }
 @Test void fieldBearingMalformedFallsBack() throws Exception { Path p=Files.createTempFile("bad", ".json"); Files.writeString(p,"{\"maxDelayMs\":321,\"hudEnabled\":false,\"hudDurationMs\":999"); assertEquals(100,new ConfigStore(p).load().maxDelayMs); Files.writeString(p,"{\"maxDelayMs\":321,\"hudEnabled\":false,\"hudDurationMs\":999} trailing"); assertEquals(100,new ConfigStore(p).load().maxDelayMs); Files.deleteIfExists(p); }
}
