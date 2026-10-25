package cn.swapdoctor.config;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.util.regex.*;
public final class ConfigStore {
 private final Path path;
 public ConfigStore(){this(Paths.get("config","swapdoctor.json"));}
 public ConfigStore(Path path){this.path=path;}
 public Path path(){return path;}
 public SwapDoctorConfig load(){
  try { if(!Files.exists(path)) return new SwapDoctorConfig(); String s=Files.readString(path,StandardCharsets.UTF_8); 
   Integer max=number(s,"maxDelayMs"), dur=number(s,"hudDurationMs"); Boolean hud=bool(s,"hudEnabled");
   if(max==null||dur==null||hud==null) return new SwapDoctorConfig(); return new SwapDoctorConfig(max,hud,dur);
  } catch(Exception e){return new SwapDoctorConfig();}
 }
 public void save(SwapDoctorConfig c){ c.validate(); try{Path p=path.toAbsolutePath().getParent();if(p!=null)Files.createDirectories(p); Files.writeString(path,"{\n  \"maxDelayMs\": "+c.maxDelayMs+",\n  \"hudEnabled\": "+c.hudEnabled+",\n  \"hudDurationMs\": "+c.hudDurationMs+"\n}\n",StandardCharsets.UTF_8);}catch(IOException e){throw new UncheckedIOException(e);} }
 private static Integer number(String s,String k){Matcher m=Pattern.compile("\\\""+k+"\\\"\\s*:\\s*(-?\\d+)").matcher(s);return m.find()?Integer.valueOf(m.group(1)):null;}
 private static Boolean bool(String s,String k){Matcher m=Pattern.compile("\\\""+k+"\\\"\\s*:\\s*(true|false)").matcher(s);return m.find()?Boolean.valueOf(m.group(1)):null;}
}
