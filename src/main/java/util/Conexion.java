package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection getConnection() {
        try {
            Class.forName(ConfiguracionDB.get("db.driver"));
            return DriverManager.getConnection(
            		ConfiguracionDB.get("db.url"),
            		ConfiguracionDB.get("db.username"),
            		ConfiguracionDB.get("db.password")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

