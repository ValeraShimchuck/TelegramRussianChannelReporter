package ua.valeriishymchuk.utils;

import java.util.TimerTask;

public final class TimerTaskUtils {

    public static TimerTask task(Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private TimerTaskUtils() {
        throw new UnsupportedOperationException();
    }
}
