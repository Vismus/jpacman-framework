package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.unit.Pellet;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;

/**
 * An extensible default interaction map for collisions caused by the player.
 * <p>
 * The implementation makes use of the interactionmap, and as such can be easily
 * and declaratively extended when new types of units (ghosts, players, ...) are
 * added.
 *
 * @author Arie van Deursen
 * @author Jeroen Roosen
 */
public class DefaultPlayerInteractionMap implements CollisionMap {

    private final CollisionMap collisions;

    public DefaultPlayerInteractionMap(Level level) {
        this.collisions = defaultCollisions(level);
    }

    @Override
    public void collide(Unit mover, Unit movedInto) {

        collisions.collide(mover, movedInto);
    }

    /**
     * Creates the default collisions Player-Ghost and Player-Pellet.
     *
     * @return The collision map containing collisions for Player-Ghost and
     * Player-Pellet.
     */
    private static CollisionInteractionMap defaultCollisions(Level level) {
        CollisionInteractionMap collisionMap = new CollisionInteractionMap();

        collisionMap.onCollision(Player.class, Ghost.class,
            (player, ghost) -> {
                if (ghost.isAlive()) {
                    if (ghost.getGameMode() == 0) {
                        player.setAlive(false);
                    } else {
                        ghost.setAlive(false);
                        ghost.setGameMode((byte) 0);
                        level.scheduleReborn(ghost, 5000);
                        player.setConsecutiveKills(player.getConsecutiveKills() + 1);
                        player.addPoints((int) (Math.pow(2, player.getConsecutiveKills()) * 100));
                    }
                }
            });

        collisionMap.onCollision(Player.class, Pellet.class,
            (player, pellet) -> {
                pellet.leaveSquare();
                player.addPoints(pellet.getValue());
                if (pellet.isPowerPellet()) {
                    level.setGameMode((byte) 1);
                }
            });
        return collisionMap;
    }
}
