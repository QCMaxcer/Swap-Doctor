# Swap Doctor manual smoke test

This checklist validates the client-only timing behavior in a Minecraft 1.21.11 Fabric client.

## Launch

1. Run `./gradlew runClient` with Java 21 or a newer JDK that supports `--release 21`.
2. Create or enter a local world and confirm the normal HUD is visible.
3. Press **K** and confirm the Cloth Config screen opens without Mod Menu.

## Settings and persistence

- Set the maximum timing window to `1`, save, reopen the screen, and confirm it remains `1`.
- Set the maximum timing window to `1000`, save, reopen, and confirm it remains `1000`.
- Toggle HUD visibility off/on and confirm the next result is hidden/shown accordingly.
- Change HUD duration, save, restart the client, and confirm the value persists in `config/swapdoctor.json`.
- If Mod Menu is installed, open Swap Doctor's config button and confirm it opens the same screen.

## Input and timing matrix

Use a number-key or mouse-wheel hotbar change, or **F** hand swap, followed by the listed mouse input. Repeat with the mouse input first.

| Case | Expected result |
| --- | --- |
| Hotbar + attack, same tick, under threshold | Green same-tick candidate, `HOTBAR`, `ATTACK`/`LEFT` |
| Hotbar + use, same tick, under threshold | Green same-tick candidate, `HOTBAR`, `USE`/`RIGHT` |
| F hand swap + left/right click, same tick | Green `TIMING_PASS`, `HAND_SWAP` |
| Either order, exactly at the threshold | Green pass (inclusive boundary) |
| Either order, over the threshold in the same tick | No result (stale events expire silently) |
| Either order, within the time window but in adjacent ticks | Red `TIMING_FAIL_CROSS_TICK` |
| Two possible matches near one event | Confirm nearest event is consumed once; no event is reused |

Check that the HUD shows only the signed millisecond delta. The green state is a client-side timing candidate, not server-side confirmation of mixed attributes.

## Filtering and lifecycle

- Repeat clicks while a screen, chat, pause menu, or lost window focus is active: no result.
- Change an unrelated inventory slot: no result.
- Die, disconnect, change world, or leave/re-enter gameplay with a pending event: pending state and HUD result clear.

## Evidence log

Record the client version, Java version, config file contents, and a screenshot of each boundary/failure case. Automated verification for this checkout:

- `./gradlew clean check build --no-daemon` — passed.
- `./gradlew runClient --no-daemon` — reached the render-thread/title startup without Swap Doctor entrypoint or mixin errors; interactive cases above still require a playable in-world session.
- Artifact SHA-256 (`build/libs/swap-doctor-1.0.0.jar`): `FB1EDD26FD578794A8A094C4275720B53BB78BDE4FC6A30E8CD8C46543FCB856`.
