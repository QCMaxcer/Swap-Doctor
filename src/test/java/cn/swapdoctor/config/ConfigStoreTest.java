package cn.swapdoctor.config;
import org.junit.jupiter.api.*;import java.nio.file.*;import static org.junit.jupiter.api.Assertions.*;
class ConfigStoreTest { @Test void roundTripAndClamp() throws Exception {Path p=Files.createTempDirectory("sd").resolve("swapdoctor.json");var s=new ConfigStore(p);var c=new SwapDoctorConfig(0,false,999999);s.save(c);var x=s.load();assertEquals(1,x.maxDelayMs);assertFalse(x.hudEnabled);assertEquals(60000,x.hudDurationMs);}
 @Test void malformedFallsBack(){var p=Path.of("build","bad-config.json");try{Files.writeString(p,"not json");var x=new ConfigStore(p).load();assertEquals(100,x.maxDelayMs);}catch(Exception e){fail(e);}finally{try{Files.deleteIfExists(p);}catch(Exception ignored){}}}}
