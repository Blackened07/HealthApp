package ru.HealthApp.utils;

import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {

    public static final String SECRET = "HealthApp.app.secret";
    public static final String ADMIN_EMAIL =  "spring.mail.username";
    private static final Properties PROPERTIES = new Properties();

    private PropertiesUtil() {
    }
    
    static {
        loadProperties();
    }

    public static String getProperty(String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }

    private static void loadProperties() {

        try(InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
