package sql.connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {

    public Connection connect() {
        System.out.println("Connecting to the database...");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql:aws://ppa-coursework-4.cn2gi2oeogwn.eu-west-1.rds.amazonaws.com:3306/covid_london_db", "admin", "enzobestetti");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n Could not connect to the database.");
        }
        return conn;
    }
}
