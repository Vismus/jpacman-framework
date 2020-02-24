package nl.tudelft.jpacman.common;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Basic implementation of ground square.
 */
public class BasicGround extends Square {

    /**
     * Creates a new basic ground square.
     */
    public BasicGround() {
        super();
    }

    @Override
    public boolean isAccessibleTo(Unit unit) {
        return true;
    }

    @Override
    @SuppressWarnings("return.type.incompatible")
    public Sprite getSprite() {
        return null;
    }
}
