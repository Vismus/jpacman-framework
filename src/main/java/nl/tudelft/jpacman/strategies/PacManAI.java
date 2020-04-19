package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.unit.Fruit;
import nl.tudelft.jpacman.level.unit.Pellet;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.*;
import java.util.stream.Collectors;

public final class PacManAI {

    /**
     * Check if the square is an intersection
     *
     * @param player the player to check if he can access to the neighbors of the current square
     * @param currentSquare the current square
     * @param direction the current direction of the player
     * @return true if the square is an intersection, otherwise return false.
     */
    public static boolean isIntersection(Player player, Square currentSquare, Direction direction) {
        return
            currentSquare.getSquareAt(direction.getClockwise()).isAccessibleTo(player)
            || currentSquare.getSquareAt(direction.getOppositeClockwise()).isAccessibleTo(player);
    }

    public static Ghost findNearestGhost(Square currentLocation) {
        List<Square> toDo = new ArrayList<>();
        Set<Square> visited = new HashSet<>();

        toDo.add(currentLocation);

        while (!toDo.isEmpty()) {
            Square square = toDo.remove(0);
            Ghost ghost = Navigation.findUnit(Ghost.class, square);
            if (ghost != null && ghost.isAlive()) {
                assert ghost.hasSquare();
                return ghost;
            }
            visited.add(square);
            for (Direction direction : Direction.values()) {
                Square newTarget = square.getSquareAt(direction);
                if (!visited.contains(newTarget) && !toDo.contains(newTarget)) {
                    toDo.add(newTarget);
                }
            }
        }

        return null;
    }

    public static Map<Square, List<Direction>> getPathToNextIntersections(Player player, List<Direction> possibleDirections) {
        List<Square> listNextIntersections = possibleDirections.stream()
            .map(direction -> findNextIntersection(player, player.getSquare().getSquareAt(direction), direction))
            .collect(Collectors.toList());

        Map<Square, List<Direction>> pathToIntersections = new HashMap<>(listNextIntersections.size());

        for (Square square : listNextIntersections) {
            List<Direction> directions = Navigation.shortestPath(player.getSquare(), square, player);
            if (directions.size() > 0 && possibleDirections.contains(directions.get(0))) {
                pathToIntersections.put(square, directions);
            }
        }

        return pathToIntersections;
    }

    /**
     * Find the next intersection square.
     *
     * @param player the player to check if he can access to a direction.
     * @param from the first square to start the research.
     * @param direction the current direction.
     * @return the square which has an intersection.
     */
    private static Square findNextIntersection(Player player, Square from, Direction direction) {
        Square square = from;
        while (!isIntersection(player, square, direction)) {
            square = square.getSquareAt(direction);
        }

        return square;
    }

    public static Map<Square, List<Direction>> getUnBlockedPath(Square from, Set<Ghost> ghosts, Map<Square, List<Direction>> possiblePaths) {
        List<Square> ghostSquares = ghosts.stream()
            .map(Unit::getSquare)
            .collect(Collectors.toList());

        return possiblePaths.entrySet().parallelStream()
            .filter(set -> noGhostInPath(from, set.getValue(), ghostSquares))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Check if no ghost block the path.
     *
     * @param from the begining square of the path.
     * @param directions list of directions for the path.
     * @param ghostSquares list the squares which contains at least a ghost.
     * @return true if no ghost block the path, otherwise return false.
     */
    private static boolean noGhostInPath(Square from, List<Direction> directions, List<Square> ghostSquares) {
        Square s = from;

        for (Direction direction : directions) {
            s = s.getSquareAt(direction);
            if (ghostSquares.contains(s)) {
                return false;
            }
        }

        return true;
    }

    public static Direction fleeNearestGhost(Square currentSquare, List<Direction> possibleDirections) {
        Unit ghost = Navigation.findNearest(Ghost.class, currentSquare);

        if (ghost == null) {
            return possibleDirections.get(0);
        }

        assert ghost.hasSquare();
        List<Direction> nearestGhost = Navigation.shortestPath(ghost.getSquare(), currentSquare, ghost);

        Direction ghostDirection = nearestGhost.get(0).getOpposite();

        if (possibleDirections.contains(ghostDirection.getOpposite())) {
            return ghostDirection.getOpposite();
        }
        possibleDirections.remove(ghostDirection);

        return possibleDirections.get(0);
    }

    public static List<Direction> getPossibleDirections(Player player) {
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (player.getSquare().getSquareAt(d).isAccessibleTo(player)) {
                possibleDirections.add(d);
            }
        }

        return possibleDirections;
    }

    public static Direction maximizePoints(Player player, List<List<Direction>> paths) {
        Unit fruit = Navigation.findNearest(Fruit.class, player.getSquare());

        if (fruit != null) {
            return maximizeFruits(player, paths, fruit);
        }

        Unit pellet = Navigation.findNearest(Pellet.class, player.getSquare());
        assert pellet != null;
        return maximizePellets(player, paths, pellet);
    }

    private static Direction maximizeFruits(Player player, List<List<Direction>> paths, Unit fruit) {
        List<Direction> nearestFruit = Navigation.shortestPath(player.getSquare(), fruit.getSquare(), player);
        assert nearestFruit != null;
        Direction directionForFruit = nearestFruit.get(0);

        List<Direction> possibleDirections = paths.parallelStream()
            .map(directions -> directions.get(0))
            .collect(Collectors.toList());

        if (possibleDirections.contains(directionForFruit)) {
            return directionForFruit;
        }

        return possibleDirections.get(0);
    }

    private static Direction maximizePellets(Player player, List<List<Direction>> paths, Unit nearestPellet) {
        int maxNbPellets = 0;
        Direction directionSelected = paths.get(0).get(0);

        for (List<Direction> path : paths) {
            Square square = player.getSquare();
            int nbPellets = 0;

            for (Direction d : path) {
                square = square.getSquareAt(d);
                if (square.getOccupants().stream().anyMatch(unit -> unit instanceof Pellet)) {
                    nbPellets += 1;
                }
            }

            if (nbPellets > maxNbPellets) {
                maxNbPellets = nbPellets;
                directionSelected = path.get(0);
            }
        }

        if (maxNbPellets == 0) {
            List<Direction> pathToNearestPellet = Navigation.shortestPath(player.getSquare(), nearestPellet.getSquare(), player);
            return paths.stream()
                .map(directions -> directions.get(0))
                .filter(direction -> direction.equals(pathToNearestPellet.get(0)))
                .findFirst().orElse(directionSelected);
        }

        return directionSelected;
    }

    public static Map<Square, List<Direction>> getSafePaths(Set<Ghost> ghosts, Map<Square, List<Direction>> possiblePaths) {
        return possiblePaths.entrySet().parallelStream()
            .filter(setPath -> {
                long size = ghosts.stream()
                    .map(ghost -> Navigation.shortestPath(ghost.getSquare(), setPath.getKey(), ghost))
                    .filter(directions -> directions != null && directions.size() <= setPath.getValue().size())
                    .count();
                return size == 0;
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<List<Direction>> getWorthPaths(Square from, Set<Ghost> ghosts, Map<Square, List<Direction>> dangerousPaths) {
        List<List<Direction>> worthPaths = new ArrayList<>(dangerousPaths.size());
        Map<Ghost, Integer> ghostSquareAndDistanceToPacman = calculateDistancesByGhost(ghosts, from);

        for (Map.Entry<Square, List<Direction>> playerEntry : dangerousPaths.entrySet()) {
            boolean canTakeThisPath = true;
            Square destination = playerEntry.getKey();
            List<Direction> pacmanToDestination = playerEntry.getValue();

            for (Map.Entry<Ghost, Integer> ghostEntry : ghostSquareAndDistanceToPacman.entrySet()) {
                Ghost g = ghostEntry.getKey();
                int distanceToPacman = ghostEntry.getValue();

                List<Direction> ghostPathToDestination = Navigation.shortestPath(g.getSquare(), destination, g);
                assert ghostPathToDestination != null;
                int ghostDistanceToDestination = ghostPathToDestination.size();
                if (!(distanceToPacman < ghostDistanceToDestination || pacmanToDestination.size() < ghostDistanceToDestination)) {
                    canTakeThisPath = false;
                    break;
                }
            }

            if (canTakeThisPath && isPelletPath(from, pacmanToDestination)) {
                worthPaths.add(pacmanToDestination);
            }
        }

        return worthPaths;
    }

    private static Map<Ghost, Integer> calculateDistancesByGhost(Set<Ghost> ghosts, Square from) {
        Map<Ghost, Integer> ghostSquareAndDistanceToPacman = new HashMap<>(ghosts.size());

        for (Ghost g : ghosts) {
            List<Direction> directions = Navigation.shortestPath(g.getSquare(), from, g);
            assert directions != null;
            ghostSquareAndDistanceToPacman.put(g, directions.size());
        }

        return ghostSquareAndDistanceToPacman;
    }

    private static boolean isPelletPath(Square from, List<Direction> path) {
        Square square = from;

        for (Direction d : path) {
            square = square.getSquareAt(d);
            if (square.getOccupants().stream().anyMatch(unit -> unit instanceof Pellet)) {
                return true;
            }
        }

        return false;
    }
}
