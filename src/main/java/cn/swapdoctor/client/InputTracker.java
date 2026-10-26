package cn.swapdoctor.client;

import cn.swapdoctor.config.ConfigStore;
import cn.swapdoctor.core.ActionButton;
import cn.swapdoctor.core.ActionEvent;
import cn.swapdoctor.core.SwapKind;
import cn.swapdoctor.core.SwapMatcher;
import cn.swapdoctor.core.SwitchEvent;
import cn.swapdoctor.core.TimingResult;
import java.util.Optional;
import net.minecraft.client.Minecraft;

/** Bridges client input hooks to the Minecraft-free timing matcher. */
public final class InputTracker {
    public static final InputTracker INSTANCE = new InputTracker();
    private final InputGate gate = new InputGate();
    private SwapMatcher matcher;
    private Optional<TimingResult> latest = Optional.empty();
    private long tickId;
    private boolean inputContext;

    public InputTracker() { matcher = new SwapMatcher(new ConfigStore().load().maxDelayMs); }
    public InputGate gate() { return gate; }
    public long tick() { return tickId; }
    public boolean inInputContext() { return inputContext; }

    public void beginTick(Minecraft client) {
        tickId++;
        updateGate(client);
        if (!gate.allow()) clearState();
    }

    public void endTick(Minecraft client) {
        updateGate(client);
        matcher.expire(System.nanoTime());
        if (client.player != null && client.player.isDeadOrDying()) clearState();
    }

    public void beginInputContext() { inputContext = true; }
    public void endInputContext() { inputContext = false; }

    public void recordMousePress(ActionButton button, long eventTick, long nanoTime) {
        if (!gate.allow()) return;
        matcher.onAction(new ActionEvent(nanoTime, eventTick, button)).ifPresent(result -> latest = Optional.of(result));
    }

    public void recordHotbarSwitch(int fromSlot, int toSlot, long eventTick, long nanoTime) {
        if (!gate.allow() || fromSlot == toSlot) return;
        matcher.onSwitch(new SwitchEvent(nanoTime, eventTick, SwapKind.HOTBAR))
                .ifPresent(result -> latest = Optional.of(result));
    }

    public void recordHandSwap(long eventTick, long nanoTime) {
        if (!gate.allow()) return;
        matcher.onSwitch(new SwitchEvent(nanoTime, eventTick, SwapKind.HAND_SWAP))
                .ifPresent(result -> latest = Optional.of(result));
    }

    public void clearState() { matcher.reset(); latest = Optional.empty(); }
    public Optional<TimingResult> latestResult() { return latest; }
    public void setThreshold(int maxDelayMs) { matcher = new SwapMatcher(maxDelayMs); latest = Optional.empty(); }

    private void updateGate(Minecraft client) {
        gate.setGameplay(client.player != null && client.level != null);
        gate.setFocused(client.isWindowActive());
        gate.setPaused(client.isPaused());
        gate.setScreenOpen(client.screen != null);
    }
}
