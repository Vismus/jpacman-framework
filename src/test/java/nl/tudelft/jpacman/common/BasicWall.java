package nl.tudelft.jpacman.common;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Basic implementation of wall square.
 */
public class BasicWall extends Square {

    /**
     * Creates a new basic wall square.
     */
    public BasicWall() {
        super();
    }

    @Override
    public boolean isAccessibleTo(Unit unit) {
        return false;
    }

    @Override
    @SuppressWarnings("return.type.incompatible")
    public Sprite getSprite() {
        return null;
    }
}
