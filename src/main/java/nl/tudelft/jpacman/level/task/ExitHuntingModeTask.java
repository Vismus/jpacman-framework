package nl.tudelft.jpacman.level.task;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.npc.Ghost;

/**
 * A task managing the gradual exit from hunting mode.
 */
public final class ExitHuntingModeTask extends Task {

    /**
     * Constructor of the class
     *
     * @param service The ScheduledTaskService associated with this task
     * @param level   The level
     */
    public ExitHuntingModeTask(ScheduledTaskService service, Level level) {
        super(service, level);
    }

    @Override
    public void run() {
        if (this.level.getGameMode() == 1) {
            this.setGameMode((byte) 2);
            service.schedule(this, 2000, true);
        } else if (this.level.getGameMode() == 2) {
            this.setGameMode((byte) 0);
        }
    }

    private void setGameMode(byte b) {
        this.level.setGameMode(b);
        for (Ghost ghost : this.level.getGhosts()) {
            if (ghost.isAlive() && ghost.getGameMode() == (b + 2) % 3) {
                ghost.setGameMode(b);
            }
        }
    }
}
