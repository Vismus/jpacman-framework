package nl.tudelft.jpacman.strategies;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to keep track of the path.
 *
 * @author Jeroen Roosen
 */
 final class Node {

    /**
     * The direction for this node, which is <code>null</code> for the root
     * node.
     */
    private final Direction direction;

    /**
     * The parent node, which is <code>null</code> for the root node.
     */
    private final Node parent;

    /**
     * The square associated with this node.
     */
    private final Square square;

    /**
     * Creates a new node.
     *
     * @param direction The direction, which is <code>null</code> for the root
     *                  node.
     * @param square    The square.
     * @param parent    The parent node, which is <code>null</code> for the root
     *                  node.
     */
    Node(Direction direction, Square square, Node parent) {
        this.direction = direction;
        this.square = square;
        this.parent = parent;
    }

    /**
     * @return The direction for this node, or <code>null</code> if this
     * node is a root node.
     */
    protected Direction getDirection() {
        return direction;
    }

    /**
     * @return The square for this node.
     */
    protected Square getSquare() {
        return square;
    }

    /**
     * @return The parent node, or <code>null</code> if this node is a root
     * node.
     */
    protected Node getParent() {
        return parent;
    }

    /**
     * Returns the list of values from the root of the tree to this node.
     *
     * @return The list of values from the root of the tree to this node.
     */
    protected List<Direction> getPath() {
        if (parent == null) {
            return new ArrayList<>();
        }
        List<Direction> path = parent.getPath();
        path.add(getDirection());
        return path;
    }
}
