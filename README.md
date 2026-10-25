# Swap Doctor

Swap Doctor is a client-only Fabric 1.21.11 practice and diagnostic mod for attribute-swapping timing.

It reports **local timing only**: a timing pass means that a local switch and mouse press were observed in the same client tick and within the configured time window. It does **not** confirm damage, enchantment, totem consumption, packet ordering, or any server-side attribute effects.

## Development

- Minecraft: 1.21.11
- Java: 21 (Gradle can run on a newer JVM with `--release 21`)
- Build: `./gradlew build`
- Checks: `./gradlew check`

Cloth Config is required at runtime for settings. Mod Menu is optional and compile-only; the default K key opens the settings screen.

Build requires a Java 21 JDK available via JAVA_HOME (or another Java 21-compatible Gradle runtime); no machine-specific JDK path is configured.
