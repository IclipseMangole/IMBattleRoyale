package de.Iclipse.BARO.Util.executor.types;


import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.Util.executor.Callback;
import de.Iclipse.IMAPI.Util.executor.IExecutor;
import de.Iclipse.IMAPI.Util.executor.ResultRunnable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BukkitExecutor extends IExecutor {

    @Override
    public Callback execute(final Runnable runnable, final Callback callback) {
        Bukkit.getScheduler().runTask(Data.instance, () -> {
            runnable.run();
            callback.done();
        });
        return callback;
    }

    @Override
    public Callback executeAsync(final Runnable runnable, final Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Data.instance, () -> {
            runnable.run();
            callback.done();
        });
        return callback;
    }

    @Override
    public Callback repeat(Consumer<Integer> consumer, int times, long interval, TimeUnit timeUnit, Callback callback) {
        TimerTask timer = new TimerTask(interval, timeUnit);
        timer.task = Bukkit.getScheduler().runTaskTimer(Data.instance, () -> {
            if (times == timer.count) {
                timer.cancelTask();
                callback.done();
                return;
            }

            consumer.accept(timer.count);
            timer.countUp();
        }, 0, timer.interval);
        return callback;
    }

    @Override
    public Callback repeatAsync(Consumer<Integer> consumer, int times, long interval, TimeUnit timeUnit, Callback callback) {
        TimerTask timer = new TimerTask(interval, timeUnit);
        timer.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Data.instance, () -> {
            if (times == timer.count) {
                timer.cancelTask();
                callback.done();
                return;
            }

            consumer.accept(timer.count);
            timer.countUp();
        }, 0, timer.interval);
        return callback;
    }

    @Override
    public Callback executeLater(Runnable runnable, long delay, TimeUnit timeUnit, Callback callback) {
        if (timeUnit != null) {
            delay = timeUnit.toSeconds(delay) * 20;
        }
        Bukkit.getScheduler().runTaskLater(Data.instance, () -> {
            runnable.run();
            callback.done();
        }, delay);
        return callback;
    }

    @Override
    public Callback executeLaterAsync(Runnable runnable, long delay, TimeUnit timeUnit, Callback callback) {
        if (timeUnit != null) {
            delay = timeUnit.toSeconds(delay) * 20;
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(Data.instance, () -> {
            runnable.run();
            callback.done();
        }, delay);
        return callback;
    }

    @Override
    public Callback executeResult(final ResultRunnable runnable, final Callback callback) {
        Bukkit.getScheduler().runTask(Data.instance, () -> {
            runnable.run();
            if (runnable.hadSuccess())
                callback.done();
        });
        return callback;
    }

    @Override
    public Callback executeResultAsync(final ResultRunnable runnable, final Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Data.instance, () -> {
            runnable.run();
            if (runnable.hadSuccess())
                callback.done();
        });
        return callback;
    }

    @Override
    public boolean isAsyncThread() {
        return !Bukkit.isPrimaryThread();
    }

    private class TimerTask {
        public BukkitTask task;
        public int count;
        public long interval;

        public TimerTask(long interval, TimeUnit unit) {
            if (unit == null) {
                this.interval = interval;
                return;
            }
            this.interval = unit.toSeconds(interval) * 20;
        }

        public void countUp() {
            count += 1;
        }

        public void cancelTask() {
            task.cancel();
        }
    }
}
