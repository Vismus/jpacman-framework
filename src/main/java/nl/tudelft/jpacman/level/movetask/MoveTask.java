package nl.tudelft.jpacman.level.movetask;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Level;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Abstract class representing a move task.
 */
public abstract class MoveTask implements Runnable {
    protected final ScheduledExecutorService service;
    protected final Unit unit;
    protected final Level level;

    MoveTask(ScheduledExecutorService service, Unit unit, Level level) {
        this.service = service;
        this.unit = unit;
        this.level = level;
    }

    /**
     * Get the service executing the move task
     *
     * @return the service executing the move task
     */
    public ScheduledExecutorService getService() {
        return service;
    }

    /**
     * Get the unit to move
     *
     * @return the unit to move
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Get the level on which the unit is moved
     *
     * @return the level on which the unit is moved
     */
    public Level getLevel() {
        return level;
    }
}
