package utils.sql.connector;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class is responsible for connecting to the database.
 * It uses the DriverManager class to get a connection to the database.
 * The connect method returns a Connection object, which can be used to execute queries.
 * The connect method is public and static so it can be called without creating an instance of the class anywhere in
 * this application.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.27
 */
public class DatabaseConnector {

    /**
     * This method connects to the database using the DriverManager class.
     * It returns a Connection object, which can be used to execute queries.
     *
     * @return Connection object
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql:aws://ppa-coursework-4.cn2gi2oeogwn.eu-west-1.rds.amazonaws.com:3306/covid_london_db",
                    "admin", "enzobestetti");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n Could not connect to the database.");
        }
        return conn;
    }
}
