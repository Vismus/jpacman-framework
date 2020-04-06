package nl.tudelft.jpacman.level.task;

import nl.tudelft.jpacman.level.Level;

/**
 * Abstract class representing a runnable task.
 */
public abstract class Task implements Runnable {
    protected final ScheduledTaskService service;
    protected final Level level;

    Task(ScheduledTaskService service, Level level) {
        this.service = service;
        this.level = level;
    }

    /**
     * Get the service executing the move task
     *
     * @return the service executing the move task
     */
    public ScheduledTaskService getService() {
        return service;
    }

    /**
     * Get the level on which the task is executed
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }
}
