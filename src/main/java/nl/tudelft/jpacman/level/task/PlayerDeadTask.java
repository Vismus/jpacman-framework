package nl.tudelft.jpacman.level.task;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.unit.Player;

/**
 * This class is used to show the animation when the player is killed by a ghost.
 */
public class PlayerDeadTask extends Task {
    private final Player player;

    public PlayerDeadTask(ScheduledTaskService service, Player player, Level level) {
        super(service, level);
        this.player = player;
    }

    @Override
    public void run() {
        this.player.leaveSquare();
        this.level.respawnGhostsInInitialPosition();
        this.player.occupy(this.player.getInitialPostion());
        this.player.setAlive(true);
        this.level.start();
    }
}
