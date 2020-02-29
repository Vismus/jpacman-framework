package nl.tudelft.jpacman;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigurationLoader {

    private static Properties properties;

    public static void load(String file) {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
}
