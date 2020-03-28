package nl.tudelft.jpacman.level.movetask;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.unit.Player;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A task that moves a Player and reschedules itself after it finished.
 */
public final class PlayerMoveTask extends MoveTask {

    public PlayerMoveTask(ScheduledExecutorService service, Player player, Level level) {
        super(service, player, level);
    }

    @Override
    public void run() {
        Direction direction = this.unit.getDirection();
        if (direction != null) {
            this.level.move(this.unit, direction);
        }
        long interval = ((Player) this.unit).getInterval();
        service.schedule(this, interval, TimeUnit.MILLISECONDS);
    }
}
