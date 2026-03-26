package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class InitDB {
    public static void runScript() {
        try (Connection conn = Conexion.getConnection();
             InputStream input = InitDB.class.getClassLoader().getResourceAsStream("init.sql");
             Scanner scanner = new Scanner(input)) {

            scanner.useDelimiter(";");

            Statement stmt = conn.createStatement();
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

