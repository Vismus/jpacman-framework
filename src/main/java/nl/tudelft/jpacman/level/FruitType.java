package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.ConfigurationLoader;

import java.util.Random;

public enum FruitType {
    CHERRY(Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.cherry.value"))),
    STRAWBERRY(Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.strawberry.value"))),
    ORANGE(Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.orange.value"))),
    APPLE(Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.apple.value"))),
    MELON(Integer.parseInt(ConfigurationLoader.getProperty("fruit.factory.melon.value")));

    private static final FruitType[] FRUITS = values();
    private static final int SIZE = FRUITS.length;
    private static final Random RANDOM = new Random();

    private int value;

    FruitType(int value) {
        this.value = value;
    }

    /**
     * Pick randomly a fruit type
     *
     * https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum
     *
     * @return the selected fruit type
     */
    public static FruitType getRamdomFruitType()  {
        return FRUITS[RANDOM.nextInt(SIZE)];
    }

    /**
     * @return the value of the fruit
     */
    public int getValue() {
        return value;
    }
}
