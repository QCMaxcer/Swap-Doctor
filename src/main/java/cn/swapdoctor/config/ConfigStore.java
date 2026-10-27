package cn.swapdoctor.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigStore {
    private static final Pattern ENTRY = Pattern.compile(
            "\\\"([A-Za-z][A-Za-z0-9]*)\\\"\\s*:\\s*(true|false|-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?|\\\"([A-Za-z_]+)\\\")");
    private final Path path;

    public ConfigStore() {
        this(Paths.get("config", "swapdoctor.json"));
    }

    public ConfigStore(Path path) {
        this.path = path;
    }

    public Path path() {
        return path;
    }

    public SwapDoctorConfig load() {
        try {
            if (!Files.exists(path)) return new SwapDoctorConfig();
            String source = Files.readString(path, StandardCharsets.UTF_8).trim();
            Map<String, String> values = parse(source);
            if (!isAccepted(values)) return new SwapDoctorConfig();
            SwapDoctorConfig config = new SwapDoctorConfig();
            config.maxDelayMs = Integer.parseInt(values.get("maxDelayMs"));
            config.hudEnabled = parseBoolean(values.get("hudEnabled"));
            config.hudDurationMs = Integer.parseInt(values.get("hudDurationMs"));
            if (values.containsKey("showDecimals")) config.showDecimals = parseBoolean(values.get("showDecimals"));
            if (values.containsKey("showScaleValues")) config.showScaleValues = parseBoolean(values.get("showScaleValues"));
            if (values.containsKey("scaleHudEnabled")) config.scaleHudEnabled = parseBoolean(values.get("scaleHudEnabled"));
            if (values.containsKey("hudMode")) config.hudMode = HudDisplayMode.fromSerialized(values.get("hudMode"));
            if (values.containsKey("scaleHudMode")) config.scaleHudMode = HudDisplayMode.fromSerialized(values.get("scaleHudMode"));
            if (values.containsKey("scaleHudDurationMs")) config.scaleHudDurationMs = Integer.parseInt(values.get("scaleHudDurationMs"));
            if (values.containsKey("hudX")) config.hudX = Float.parseFloat(values.get("hudX"));
            if (values.containsKey("hudY")) config.hudY = Float.parseFloat(values.get("hudY"));
            if (values.containsKey("scaleX")) config.scaleX = Float.parseFloat(values.get("scaleX"));
            if (values.containsKey("scaleY")) config.scaleY = Float.parseFloat(values.get("scaleY"));
            if (values.containsKey("scaleWidth")) config.scaleWidth = Integer.parseInt(values.get("scaleWidth"));
            if (values.containsKey("scaleLengthMultiplier")) config.scaleLengthMultiplier = Float.parseFloat(values.get("scaleLengthMultiplier"));
            if (values.containsKey("scaleThicknessMultiplier")) config.scaleThicknessMultiplier = Float.parseFloat(values.get("scaleThicknessMultiplier"));
            if (values.containsKey("positiveDirection")) config.positiveDirection = PositiveDirection.fromSerialized(values.get("positiveDirection"));
            config.validate();
            return config;
        } catch (Exception ignored) {
            return new SwapDoctorConfig();
        }
    }

    public void save(SwapDoctorConfig config) {
        config.validate();
        String content = "{\n"
                + "  \"maxDelayMs\": " + config.maxDelayMs + ",\n"
                + "  \"hudEnabled\": " + config.hudEnabled + ",\n"
                + "  \"hudDurationMs\": " + config.hudDurationMs + ",\n"
                + "  \"showDecimals\": " + config.showDecimals + ",\n"
                + "  \"showScaleValues\": " + config.showScaleValues + ",\n"
                + "  \"scaleHudEnabled\": " + config.scaleHudEnabled + ",\n"
                + "  \"hudMode\": \"" + config.hudMode.serialized() + "\",\n"
                + "  \"scaleHudMode\": \"" + config.scaleHudMode.serialized() + "\",\n"
                + "  \"scaleHudDurationMs\": " + config.scaleHudDurationMs + ",\n"
                + "  \"hudX\": " + format(config.hudX) + ",\n"
                + "  \"hudY\": " + format(config.hudY) + ",\n"
                + "  \"scaleX\": " + format(config.scaleX) + ",\n"
                + "  \"scaleY\": " + format(config.scaleY) + ",\n"
                + "  \"scaleWidth\": " + config.scaleWidth + ",\n"
                + "  \"scaleLengthMultiplier\": " + format(config.scaleLengthMultiplier) + ",\n"
                + "  \"scaleThicknessMultiplier\": " + format(config.scaleThicknessMultiplier) + ",\n"
                + "  \"positiveDirection\": \"" + config.positiveDirection.serialized() + "\"\n"
                + "}\n";
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String format(float value) {
        return String.format(java.util.Locale.ROOT, "%.6f", value);
    }

    private static boolean isAccepted(Map<String, String> values) {
        if (values == null || !values.containsKey("maxDelayMs") || !values.containsKey("hudEnabled") || !values.containsKey("hudDurationMs")) {
            return false;
        }
        for (String key : values.keySet()) {
            if (!KNOWN_KEYS.contains(key)) return false;
        }
        return true;
    }

    private static final java.util.Set<String> KNOWN_KEYS = java.util.Set.of(
            "maxDelayMs", "hudEnabled", "hudDurationMs", "showDecimals", "showScaleValues", "scaleHudEnabled", "hudMode",
            "scaleHudMode", "scaleHudDurationMs", "hudX", "hudY", "scaleX", "scaleY", "scaleWidth",
            "scaleLengthMultiplier", "scaleThicknessMultiplier", "positiveDirection");

    private static boolean parseBoolean(String value) {
        if ("true".equals(value)) return true;
        if ("false".equals(value)) return false;
        throw new IllegalArgumentException("Expected boolean");
    }

    private static Map<String, String> parse(String source) {
        if (source.length() < 2 || source.charAt(0) != '{' || source.charAt(source.length() - 1) != '}') return null;
        String body = source.substring(1, source.length() - 1).trim();
        if (body.isEmpty()) return null;
        Map<String, String> values = new HashMap<>();
        for (String entry : split(body)) {
            Matcher matcher = ENTRY.matcher(entry.trim());
            if (!matcher.matches()) return null;
            String value = matcher.group(3) != null ? matcher.group(3) : matcher.group(2);
            if (values.put(matcher.group(1), value) != null) return null;
        }
        return values;
    }

    private static List<String> split(String source) {
        List<String> entries = new ArrayList<>();
        int start = 0;
        boolean quoted = false;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '"' && (i == 0 || source.charAt(i - 1) != '\\')) quoted = !quoted;
            else if (c == ',' && !quoted) {
                entries.add(source.substring(start, i));
                start = i + 1;
            }
        }
        if (quoted) return List.of();
        entries.add(source.substring(start));
        return entries;
    }
}
