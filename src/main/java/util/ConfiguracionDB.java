package util;

import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionDB {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfiguracionDB.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}

