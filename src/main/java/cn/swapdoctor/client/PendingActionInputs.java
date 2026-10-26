package cn.swapdoctor.client;

import cn.swapdoctor.core.ActionKind;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.OptionalLong;

/** Keeps raw input timestamps until Minecraft confirms the gameplay action. */
final class PendingActionInputs {
    private final ArrayDeque<Pending> pending = new ArrayDeque<>();

    void add(ActionKind kind, long tick, long nanoTime) {
        pending.addLast(new Pending(kind, tick, nanoTime));
    }

    OptionalLong consume(ActionKind kind, long tick) {
        Iterator<Pending> iterator = pending.iterator();
        while (iterator.hasNext()) {
            Pending value = iterator.next();
            if (value.kind == kind && value.tick <= tick && tick - value.tick <= 1) {
                iterator.remove();
                return OptionalLong.of(value.nanoTime);
            }
        }
        return OptionalLong.empty();
    }

    void discardBefore(long tick) {
        pending.removeIf(value -> value.tick < tick - 1);
    }

    void clear() {
        pending.clear();
    }

    private record Pending(ActionKind kind, long tick, long nanoTime) {
    }
}
