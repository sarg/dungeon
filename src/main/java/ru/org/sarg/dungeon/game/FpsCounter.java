package ru.org.sarg.dungeon.game;

import java.util.concurrent.TimeUnit;

public class FpsCounter {
    private final double targetFps;
    private long t;

    public FpsCounter(double targetFps) {
        this.targetFps = targetFps;
    }

    public boolean isReadyForNextFrame() {
        return getSleep() < 0;
    }

    public void sleepUntilNextFrame() {
        long sleep = getSleep();
        if (sleep > 0)
            try {
                Thread.sleep(TimeUnit.NANOSECONDS.toMillis(sleep));
            } catch (InterruptedException e) {
                throw new RuntimeException("Unhandled exception", e);
            }
    }

    private long getSleep() {
        return (long) (TimeUnit.SECONDS.toNanos(1) / targetFps - (System.nanoTime() - t));
    }

    public void onFrame() {
        t = System.nanoTime();
    }
}
