package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.*;
import java.util.stream.Collectors;

public class PriorityToScoreStrategy extends PacManStrategy {
    public static String TITLE = "AI - Priority to score";

    private final Player player;
    private final Set<Ghost> ghosts;

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

        Collections.shuffle(possibleDirections);

        Square currentSquare = player.getSquare();
        Ghost ghost = PacManAI.findNearestGhost(currentSquare);

        if (ghost != null) {
            List<Direction> directionToNearestGhost = Navigation.shortestPath(currentSquare, ghost.getSquare(), player);

            if (ghost.getGameMode() != 0 && directionToNearestGhost.size() > 0) {
                return directionToNearestGhost.get(0);
            }
        }

        Map<Square, List<Direction>> possiblePaths = PacManAI.getUnBlockedPath(
            currentSquare,
            ghosts,
            PacManAI.getPathToNextIntersections(player, possibleDirections));

        if (possiblePaths.isEmpty()) {
            return null; // perhaps the currentDirection ?
        } else if (possiblePaths.size() == 1) {
            return possiblePaths.values().stream().findFirst().get().get(0);
        }

        Map<Square, List<Direction>> safePaths = PacManAI.getSafePaths(ghosts, possiblePaths);
        Map<Square, List<Direction>> dangerousPaths = possiblePaths.entrySet().stream()
            .filter(entry -> safePaths.containsKey(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<List<Direction>> listSafePath = new ArrayList<>(safePaths.values());
        listSafePath.addAll(PacManAI.getWorthPaths(currentSquare, ghosts, dangerousPaths));

        if (listSafePath.isEmpty()) {
            return PacManAI.fleeNearestGhost(currentSquare, possibleDirections);
        } else if (listSafePath.size() == 1) {
            return listSafePath.get(0).get(0);
        } else {
            return PacManAI.maximizePoints(player, listSafePath);
        }
    }
}
