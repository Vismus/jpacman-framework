package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

import java.util.*;

/**
 * Navigation provides utility to navigate on {@link Square}s.
 *
 * @author Jeroen Roosen
 */
public final class Navigation {

    private Navigation() {
    }

    /**
     * Calculates the shortest path. This is done by BFS. This search ensures
     * the traveller is allowed to occupy the squares on the way, or returns the
     * shortest path to the square regardless of terrain if no traveller is
     * specified.
     *
     * @param from      The starting square.
     * @param to        The destination.
     * @param traveller The traveller attempting to reach the destination. If
     *                  traveller is set to <code>null</code>, this method will ignore
     *                  terrain and find the shortest path whether it can actually be
     *                  reached or not.
     * @return The shortest path to the destination or <code>null</code> if no
     * such path could be found. When the destination is the current
     * square, an empty list is returned.
     */
    public static List<Direction> shortestPath(Square from, Square to, Unit traveller) {
        if (from.equals(to)) {
            return new ArrayList<>();
        }

        List<Node> targets = new ArrayList<>();
        Set<Square> visited = new HashSet<>();
        targets.add(new Node(null, from, null));
        while (!targets.isEmpty()) {
            Node node = targets.remove(0);
            Square square = node.getSquare();
            if (square.equals(to)) {
                return node.getPath();
            }
            visited.add(square);
            addNewTargets(traveller, targets, visited, node, square);
        }
        return null;
    }

    private static void addNewTargets(Unit traveller, List<Node> targets, Set<Square> visited, Node node, Square square) {
        for (Direction direction : Direction.values()) {
            Square target = square.getSquareAt(direction);
            if (!visited.contains(target) && (traveller == null || target.isAccessibleTo(traveller))) {
                targets.add(new Node(direction, target, node));
            }
        }
    }

    /**
     *
     * @param from The starting square.
     * @param to The destination.
     * @param traveller The traveller attempting to reach the destination. If
     *                  traveller is set to <code>null</code>, this method will ignore
     *                  terrain and find the shortest path whether it can actually be
     *                  reached or not.
     * @return The next direction to take to get to the destination
     */
    public static Optional<Direction> getNextDirection(Square from, Square to, Unit traveller) {
        List<Direction> path = shortestPath(from, to, traveller);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }

    /**
     * Finds the nearest unit of the given type and returns its location. This
     * method will perform a breadth first search starting from the given
     * square.
     *
     * @param type            The type of unit to search for.
     * @param currentLocation The starting location for the search.
     * @return The nearest unit of the given type, or <code>null</code> if no
     * such unit could be found.
     */
    public static Unit findNearest(Class<? extends Unit> type, Square currentLocation) {
        List<Square> toDo = new ArrayList<>();
        Set<Square> visited = new HashSet<>();

        toDo.add(currentLocation);

        while (!toDo.isEmpty()) {
            Square square = toDo.remove(0);
            Unit unit = findUnit(type, square);
            if (unit != null) {
                assert unit.hasSquare();
                return unit;
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

    /**
     * Finds a subtype of Unit in a level.
     * This method is very useful for finding the ghosts in the parsed map.
     *
     * @param clazz the type to search for.
     * @param board the board to find the unit in.
     * @param <T>   the return type, same as the type in clazz.
     * @return the first unit found of type clazz, or null.
     */
    public static <T extends Unit> T findUnitInBoard(Class<T> clazz, Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                final T ghost = Navigation.findUnit(clazz, board.squareAt(x, y));
                if (ghost != null) {
                    return ghost;
                }
            }
        }

        return null;
    }

    /**
     * Determines whether a square has an occupant of a certain type.
     *
     * @param type   The type to search for.
     * @param square The square to search.
     * @param <T>    the type of unit we searched for.
     * @return A unit of type T, iff such a unit occupies this square, or
     * <code>null</code> of none does.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Unit> T findUnit(Class<T> type, Square square) {
        for (Unit unit : square.getOccupants()) {
            if (type.isInstance(unit)) {
                assert unit.hasSquare();
                return (T) unit;
            }
        }
        return null;
    }
}
