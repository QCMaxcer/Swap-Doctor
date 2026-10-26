package cn.swapdoctor.config;
import org.junit.jupiter.api.*;import java.nio.file.*;import static org.junit.jupiter.api.Assertions.*;
class ConfigStoreNumberTest { @Test void leadingZeroNumbersFallback() throws Exception { Path p=Files.createTempFile("badnum", ".json"); Files.writeString(p,"{\"maxDelayMs\":001,\"hudEnabled\":true,\"hudDurationMs\":1500}"); assertEquals(100,new ConfigStore(p).load().maxDelayMs); Files.deleteIfExists(p); } }
