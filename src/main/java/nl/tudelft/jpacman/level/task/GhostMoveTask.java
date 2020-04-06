package nl.tudelft.jpacman.level.task;

import nl.tudelft.jpacman.ConfigurationLoader;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.npc.Ghost;

/**
 * A task that moves an NPC and reschedules itself after it finished.
 */
public final class GhostMoveTask extends Task {

    private final Ghost ghost;
    private final int DEAD_MOVE_INTERVAL = Integer.parseInt(ConfigurationLoader.getProperty("ghost.dead.move.interval"));

    /**
     * Constructor of the class
     *
     * @param service The ScheduledTaskService associated with this task
     * @param ghost   The npc which must move
     * @param level   The level
     */
    public GhostMoveTask(ScheduledTaskService service, Ghost ghost, Level level) {
        super(service, level);
        this.ghost = ghost;
    }

    @Override
    public void run() {
        if (ghost.isAlive()) {
            this.moveGhost();
            service.schedule(this, this.ghost.getInterval(), true);
        } else {
            if (!ghost.getSquare().equals(ghost.getInitialPosition())) {
                this.moveGhost();
                service.schedule(this, this.DEAD_MOVE_INTERVAL, true);
            } else {
                service.schedule(this, this.ghost.getInterval(), true);
            }
        }
    }

    private void moveGhost() {
        Direction nextMove = this.ghost.nextMove();
        if (nextMove != null) {
            this.level.move(this.ghost, nextMove);
        }
    }
}
