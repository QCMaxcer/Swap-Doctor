# Swap Doctor Design

## Intent

Swap Doctor is a client-only Fabric 1.21.11 practice/diagnostic mod for the Minecraft attribute-swapping technique. It reports local input timing only: a timing pass means a switch and a left/right mouse press were observed in the same client tick and within the configured absolute time window. It does not verify damage, enchantment, totem consumption, packet ordering, or server-side attribute application.

## Scope

- Detect main-hand hotbar selection changes and main/offhand swaps triggered by the local player.
- Detect left/right mouse PRESS events while in gameplay, with no screen, pause, or focus loss.
- Support both event orders and expose signed `clickTime - switchTime` in milliseconds.
- Default threshold: 100 ms; valid range: 1–1000 ms; default HUD duration: 1500 ms.
- Use a nearest-neighbor, one-to-one queue matcher; stale/unmatched events expire silently.
- Clear pending events and the displayed result on GUI entry, focus loss, death, disconnect, or world change.

## Non-goals

- No server companion, network packet modification, automatic item switching, or effect confirmation.
- No backpack/inventory organization tracking.

## UI and dependencies

Cloth Config is a required runtime dependency for the settings screen. Mod Menu is optional; a default K key opens the same screen without Mod Menu. The HUD uses Fabric's current `HudElementRegistry` API. All user-facing strings have `zh_cn` and `en_us` translations.

## Compatibility

Use official Mojang mappings and the current Fabric 1.21.11 template dependency set. Keep exact dependency versions in `gradle.properties` and explain the timing-only semantics in README.
