package cn.swapdoctor.config;
public final class SwapDoctorConfig {
 public static final int DEFAULT_MAX_DELAY_MS=100, DEFAULT_HUD_DURATION_MS=1500;
 public int maxDelayMs; public boolean hudEnabled; public int hudDurationMs; public boolean showDecimals;
 public SwapDoctorConfig(){this(DEFAULT_MAX_DELAY_MS,true,DEFAULT_HUD_DURATION_MS,false);}
 public SwapDoctorConfig(int maxDelayMs, boolean hudEnabled, int hudDurationMs){this(maxDelayMs,hudEnabled,hudDurationMs,false);}
 public SwapDoctorConfig(int maxDelayMs, boolean hudEnabled, int hudDurationMs, boolean showDecimals){this.maxDelayMs=clamp(maxDelayMs,1,1000);this.hudEnabled=hudEnabled;this.hudDurationMs=clamp(hudDurationMs,1,60000);this.showDecimals=showDecimals;}
 public SwapDoctorConfig copy(){return new SwapDoctorConfig(maxDelayMs,hudEnabled,hudDurationMs,showDecimals);}
 public void validate(){maxDelayMs=clamp(maxDelayMs,1,1000);hudDurationMs=clamp(hudDurationMs,1,60000);}
 private static int clamp(int x,int lo,int hi){return Math.max(lo,Math.min(hi,x));}
}
