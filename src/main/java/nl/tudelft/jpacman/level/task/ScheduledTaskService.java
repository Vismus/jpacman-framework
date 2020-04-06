package nl.tudelft.jpacman.level.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskService {

    private final ScheduledExecutorService service;
    private ScheduledFuture<?> futur = null;
    private long remainingDelay;
    private Runnable command;

    /**
     * Constructor of the class
     */
    public ScheduledTaskService() {
        this.service = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Schedule a command to be executed within a certain delay. The timer can be started now or later.
     *
     * @param command  The command to be executed
     * @param delay    The delay
     * @param startNow If true, start the timer now.
     */
    public void schedule(Runnable command, long delay, boolean startNow) {
        this.command = command;
        if (startNow) {
            this.remainingDelay = 0;
            this.futur = this.service.schedule(command, delay, TimeUnit.MILLISECONDS);
        } else {
            this.remainingDelay = delay;
        }
    }

    /**
     * Suspend the timer for the scheduled task
     */
    public void suspend() {
        if (this.futur != null) {
            this.remainingDelay = this.futur.getDelay(TimeUnit.MILLISECONDS);
            this.futur.cancel(true);
        }
    }

    /**
     * Resume the timer for the scheduled task
     */
    public void resume() {
        if (this.remainingDelay != 0) {
            this.futur = this.service.schedule(this.command, this.remainingDelay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Cancel scheduled task
     */
    public void cancelTask() {
        if (this.futur != null) {
            this.futur.cancel(true);
            this.futur = null;
        }
    }
}
