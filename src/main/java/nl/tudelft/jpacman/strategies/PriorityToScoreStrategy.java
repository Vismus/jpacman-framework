package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.*;
import java.util.stream.Collectors;

public final class PriorityToScoreStrategy extends PacManStrategy {
    public static String TITLE = "AI - Priority to score";

    /**
     * The player to have all his information to compute correctly his next move.
     */
    private final Player player;

    /**
     * The list of ghosts in the board.
     */
    private final Set<Ghost> ghosts;

    /**
     * Create the Priority to score Strategy for the player.
     * It is an IA for the player.
     *
     * @param game the current game.
     * @param player the player who has this strategy.
     */
    public PriorityToScoreStrategy(Game game, Player player) {
        super(game);
        this.player = player;
        this.ghosts = game.getLevel().getGhosts();
    }

    @Override
    public Direction nextMove() {
        List<Direction> possibleDirections = PacManAI.getPossibleDirections(player);
        assert !possibleDirections.isEmpty();

        if (possibleDirections.size() == 1) {
            return possibleDirections.get(0);
        }

        if (!PacManAI.isIntersection(player, player.getSquare(), player.getDirection())) {
            return player.getDirection();
        }

        return selectBestDirection(possibleDirections);
    }

    /**
     * Select the best direction to increase a maximum
     *
     * @param possibleDirections list of possibles directions that player can take
     * @return The best direction selected
     */
    private Direction selectBestDirection(List<Direction> possibleDirections) {
        Collections.shuffle(possibleDirections);

        Direction directionToFleeNearestGhost = findDirectionToNearestFleeGhost();

        if (directionToFleeNearestGhost != null) {
            return directionToFleeNearestGhost;
        }

        return searchDirectionToOptimalPath(possibleDirections);
    }

    /**
     * Find the direction to nearest flee ghost
     *
     * @return the direction to the flee ghost, return null if no flee ghost
     */
    private Direction findDirectionToNearestFleeGhost() {
        Ghost ghost = PacManAI.findNearestGhost(player.getSquare());

        if (ghost != null) {
            List<Direction> directionToNearestGhost = Navigation.shortestPath(player.getSquare(), ghost.getSquare(), player);

            if (ghost.getGameMode() != 0 && directionToNearestGhost.size() > 0) {
                return directionToNearestGhost.get(0);
            }
        }

        return null;
    }

    /**
     * Search the direction to the optimal path for the player doesn't
     * die and increase as much as possible his score.
     *
     * @param possibleDirections list of possible directions that the player can take.
     * @return The best direction that the player must take.
     */
    private Direction searchDirectionToOptimalPath(List<Direction> possibleDirections) {
        Map<Square, List<Direction>> possiblePaths = PacManAI.getUnBlockedPath(
            player.getSquare(),
            ghosts,
            PacManAI.getPathToNextIntersections(player, possibleDirections));

        if (possiblePaths.isEmpty()) {
            return possibleDirections.get(0);
        } else if (possiblePaths.size() == 1) {
            return possiblePaths.values().stream().findFirst().get().get(0);
        }

        Map<Square, List<Direction>> safePaths = PacManAI.getSafePaths(ghosts, possiblePaths);
        Map<Square, List<Direction>> dangerousPaths = possiblePaths.entrySet().stream()
            .filter(entry -> safePaths.containsKey(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<List<Direction>> listSafePath = new ArrayList<>(safePaths.values());
        listSafePath.addAll(PacManAI.getWorthPaths(player.getSquare(), ghosts, dangerousPaths));

        if (listSafePath.isEmpty()) {
            return PacManAI.fleeNearestGhost(player.getSquare(), possibleDirections);
        } else if (listSafePath.size() == 1) {
            return listSafePath.get(0).get(0);
        } else {
            return PacManAI.maximizePoints(player, listSafePath);
        }
    }
}
