package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.level.unit.Fruit;
import nl.tudelft.jpacman.sprite.PacManSprites;

public class FruitFactory {
    /**
     * The sprite store containing the fruit sprites
     */
    private final PacManSprites sprites;

    /**
     * Creates a new fruit factory
     * @param spriteStore
     */
    public FruitFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
    }

    public Fruit createFruit() {
        FruitType fruitType = FruitType.getRamdomFruitType();
        return new Fruit(fruitType.getValue(), sprites.getFruit(fruitType));
    }

    /**
     * Create a new cherry as fruit
     *
     * @return The new cherry
     */
    public Fruit createCherry() {
        return new Fruit(FruitType.CHERRY.getValue(), sprites.getFruit(FruitType.CHERRY));
    }

    /**
     * Create a new strawberry as fruit
     *
     * @return The new strawberry
     */
    public Fruit createStrawberry() {
        return new Fruit(FruitType.STRAWBERRY.getValue(), sprites.getFruit(FruitType.STRAWBERRY));
    }

    /**
     * Create a new orange as fruit
     *
     * @return The new orange
     */
    public Fruit createOrange() {
        return new Fruit(FruitType.ORANGE.getValue(), sprites.getFruit(FruitType.ORANGE));
    }

    /**
     * Create a new apple as fruit
     *
     * @return The new apple
     */
    public Fruit createApple() {
        return new Fruit(FruitType.APPLE.getValue(), sprites.getFruit(FruitType.APPLE));
    }

    /**
     * Create a new melon as fruit
     *
     * @return The new melon
     */
    public Fruit createMelon() {
        return new Fruit(FruitType.MELON.getValue(), sprites.getFruit(FruitType.MELON));
    }
}
