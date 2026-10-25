# Task 1 Report — Fabric project scaffold and locked dependencies

Date: 2026-10-25

## Delivered

- Added Gradle 9.8.2 wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`).
- Added Fabric Loom plugin `net.fabricmc.fabric-loom-remap` `1.19-SNAPSHOT` (resolved metadata as Loom 1.19.3).
- Locked Minecraft/Fabric versions in `gradle.properties`: Minecraft 1.21.11, Loader 0.20.1, Fabric API 0.141.6+1.21.11, Cloth Config 21.11.160, optional compile-only Mod Menu 17.0.1.
- Configured official Mojang mappings and required Maven repositories.
- Added client-only `cn.swapdoctor.SwapDoctorClient` entrypoint and client-only mixin configuration; no server entrypoint is declared.
- Added metadata, README timing-only semantics, ignore rules, and retained the approved design spec unchanged.

## Verification

- `./gradlew --version`: PASS — resolved and ran Gradle 9.8.2; JVM details reported (Gradle daemon configured to use Java 25).
- `./gradlew check`: BLOCKED by upstream artifact availability. Fabric Loom resolves official Mojang mappings to `loom:mappings:layered+hash.253056520`, which is not present in the configured repositories. The failure occurs during project configuration before project sources are compiled. All requested dependency coordinates and mappings declaration remain intact.

## Commit

`122bde30b58bd93a8d97f2d68395e1f6a88e473d` — `chore: scaffold Swap Doctor Fabric project`

## Round 1 fixes (review follow-up)

- Removed machine-specific `org.gradle.java.home` from `gradle.properties`; README now documents Java 21 JDK/JAVA_HOME expectation.
- Removed restrictive `dependencyResolutionManagement` from `settings.gradle` so Loom/build repositories are not suppressed; official Mojang mappings declaration remains unchanged.
- Removed the missing icon metadata reference (minimal valid packaging fix).
- Re-ran with `JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot`: `./gradlew --version` PASS (Gradle 9.8.2, JVM 25.0.3). `./gradlew check` still fails during configuration because upstream repositories do not contain `loom:mappings:layered+hash.253056520`; no alternate mappings were substituted.

## Fix round 2 evidence

- Rewrote `settings.gradle` to retain only `pluginManagement`; removed the entire `dependencyResolutionManagement`/`PREFER_SETTINGS` block so Loom-added repositories are honored.
- Kept Cloth Config and Mod Menu repositories in `build.gradle`.
- Exact command: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot'; ./gradlew check --no-daemon`
- Result: **BUILD SUCCESSFUL in 4s** (Gradle 9.8.2, Fabric Loom 1.19.3; `:compileJava`, `:processResources`, and `:check` completed; test tasks had no sources).
- The earlier mappings resolution issue did not recur after this template-aligned settings change.
