package nl.tudelft.jpacman.level.task;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.unit.Player;

/**
 * A task that moves a Player and reschedules itself after it finished.
 */
public final class PlayerMoveTask extends Task {

    private final Player player;

    /**
     * Constructor of the class
     * @param service The ScheduledTaskService associated with this task
     * @param player The player which must move
     * @param level The level
     */
    public PlayerMoveTask(ScheduledTaskService service, Player player, Level level) {
        super(service, level);
        this.player = player;
    }

    @Override
    public void run() {
        Direction direction = this.player.nextMove();
        if (direction != null) {
            this.level.move(this.player, direction);
        }
        long interval = this.player.getInterval();
        service.schedule(this, interval, true);
    }
}
