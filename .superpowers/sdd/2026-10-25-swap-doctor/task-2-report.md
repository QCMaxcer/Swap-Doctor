# Task 2 Report — Pure timing model, matcher, config

## RED
Added focused JUnit tests for bidirectional timing, signed delta/direction, cross-tick failure, stale silence/reset, and config round-trip/malformed fallback. Initial `./gradlew test --no-daemon` reached test execution but failed before assertions because the Gradle JUnit Platform launcher runtime was absent; adding the launcher exposed the intended assertion failure (result was incorrectly read from the unmatched switch call).

## GREEN
Implemented dependency-free core records/enums and `SwapMatcher` with nearest one-to-one pairing, inclusive max-delay window, signed milliseconds, direction, same-tick pass/cross-tick fail, silent expiry, and reset. Implemented `SwapDoctorConfig` defaults/clamping and `ConfigStore` JSON persistence/fallback. Corrected tests and verified all 6 tests pass.

## Verification
`JAVA_HOME='C:\\Program Files\\Eclipse Adoptium\\jdk-25.0.3.9-hotspot'; ./gradlew test --no-daemon` — BUILD SUCCESSFUL (6 tests).
