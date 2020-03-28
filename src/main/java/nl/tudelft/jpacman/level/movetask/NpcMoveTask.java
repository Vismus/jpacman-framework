package nl.tudelft.jpacman.level.movetask;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A task that moves an NPC and reschedules itself after it finished.
 */
public final class NpcMoveTask extends MoveTask {

    public NpcMoveTask(ScheduledExecutorService service, Ghost npc, Level level) {
        super(service, npc, level);
    }

    @Override
    public void run() {
        Direction nextMove = ((Ghost) this.unit).nextMove();
        if (nextMove != null) {
            this.level.move(this.unit, nextMove);
        }
        long interval = ((Ghost) this.unit).getInterval();
        service.schedule(this, interval, TimeUnit.MILLISECONDS);
    }
}
