package cn.swapdoctor.client;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.OptionalLong;

/** Keeps raw keyboard timestamps until the corresponding slot/offhand mutation runs. */
final class PendingSwitchInputs {
    private final ArrayDeque<Pending> pending = new ArrayDeque<>();

    void add(long tick, long nanoTime) {
        pending.addLast(new Pending(tick, nanoTime));
    }

    OptionalLong consume(long currentTick) {
        Iterator<Pending> iterator = pending.descendingIterator();
        while (iterator.hasNext()) {
            Pending value = iterator.next();
            if (value.tick <= currentTick && currentTick - value.tick <= 1) {
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

    private record Pending(long tick, long nanoTime) {
    }
}
