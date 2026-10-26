package cn.swapdoctor.config;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.util.*;import java.util.regex.*;
public final class ConfigStore {
 private final Path path;
 public ConfigStore(){this(Paths.get("config","swapdoctor.json"));} public ConfigStore(Path path){this.path=path;} public Path path(){return path;}
 public SwapDoctorConfig load(){try{if(!Files.exists(path))return new SwapDoctorConfig();String s=Files.readString(path,StandardCharsets.UTF_8).trim();Map<String,String> v=parse(s);if(v==null||v.size()!=3||!v.containsKey("maxDelayMs")||!v.containsKey("hudEnabled")||!v.containsKey("hudDurationMs"))return new SwapDoctorConfig();return new SwapDoctorConfig(Integer.parseInt(v.get("maxDelayMs")),parseBool(v.get("hudEnabled")),Integer.parseInt(v.get("hudDurationMs")));}catch(Exception e){return new SwapDoctorConfig();}}
 public void save(SwapDoctorConfig c){c.validate();try{Path p=path.toAbsolutePath().getParent();if(p!=null)Files.createDirectories(p);Files.writeString(path,"{\n  \"maxDelayMs\": "+c.maxDelayMs+",\n  \"hudEnabled\": "+c.hudEnabled+",\n  \"hudDurationMs\": "+c.hudDurationMs+"\n}\n",StandardCharsets.UTF_8);}catch(IOException e){throw new UncheckedIOException(e);}}
 private static boolean parseBool(String s){if("true".equals(s))return true;if("false".equals(s))return false;throw new IllegalArgumentException();}
 private static Map<String,String> parse(String s){if(s.length()<2||s.charAt(0)!='{'||s.charAt(s.length()-1)!='}')return null;String b=s.substring(1,s.length()-1).trim();if(b.isEmpty())return null;Map<String,String> out=new HashMap<>();for(String part:split(b)){Matcher m=Pattern.compile("\\\"([A-Za-z][A-Za-z0-9]*)\\\"\\s*:\\s*(true|false|-?\\d+)").matcher(part.trim());if(!m.matches())return null;if(out.put(m.group(1),m.group(2))!=null)return null;}return out;}
 private static List<String> split(String s){List<String> r=new ArrayList<>();int start=0;boolean q=false;for(int i=0;i<s.length();i++){char c=s.charAt(i);if(c=='\"'&&(i==0||s.charAt(i-1)!='\\'))q=!q;else if(c==','&&!q){r.add(s.substring(start,i));start=i+1;}}if(q)return List.of();r.add(s.substring(start));return r;}
}
