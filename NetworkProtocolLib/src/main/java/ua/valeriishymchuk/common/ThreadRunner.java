package ua.valeriishymchuk.common;

import ua.valeriishymchuk.utils.stream.model.CheckedConsumer;
import ua.valeriishymchuk.utils.stream.model.CheckedRunnable;

public final class ThreadRunner {

    private final Thread thread;

    private ThreadRunner(CheckedRunnable runnable) {
        thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void run() {
        if(!thread.isAlive()) thread.start();
    }

    public void stop() {
        if(thread.isAlive()) thread.interrupt();
    }

    public boolean isRunning() {
        return thread.isAlive();
    }

    public static ThreadRunner of(CheckedRunnable runnable) {
        return new ThreadRunner(runnable);
    }

}
