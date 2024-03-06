package sql.connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    public Connection connect() {
        System.out.println("Connecting to the database...");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/covidlondon", "root", "root");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n Could not connect to the database.");
        }
        return conn;
    }
}
