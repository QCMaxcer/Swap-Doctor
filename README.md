# Swap Doctor

Swap Doctor is a client-only Fabric 1.21.11 practice and diagnostic mod for attribute-swapping timing.

It reports **local timing only**: a timing pass means that a local switch and mouse press were observed in the same client tick and within the configured time window. It does **not** confirm damage, enchantment, totem consumption, packet ordering, or any server-side attribute effects.

## Development

- Minecraft: 1.21.11
- Java: 21 (Gradle can run on a newer JVM with `--release 21`)
- Build: `./gradlew build`
- Checks: `./gradlew check`

Cloth Config is required at runtime for settings. Mod Menu is optional and compile-only; the default K key opens the settings screen.

## In-game use

- Press **K** in a world to open the Swap Doctor settings screen.
- Set the maximum timing window (1–1000 ms), HUD visibility, and HUD duration (1–60000 ms).
- A green HUD line is a same-tick local timing candidate (not server-side confirmation); a red line is a cross-tick failure.
- The line includes the switch kind, mouse button, signed `clickTime - switchTime`, configured threshold, and event order.
- Pending events and the displayed result are cleared when leaving gameplay, losing focus, dying, disconnecting, or changing worlds.

The complete manual acceptance matrix is in [`docs/manual-smoke-test.md`](docs/manual-smoke-test.md).

Build requires a Java 21 JDK available via JAVA_HOME (or another Java 21-compatible Gradle runtime); no machine-specific JDK path is configured.
