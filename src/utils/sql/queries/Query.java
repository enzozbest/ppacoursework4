package utils.sql.queries;

import utils.sql.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * This class is responsible for creating a query object that can be used to execute a SQL query on a database.
 * <p>
 * The methods in this class can be used by an ExecutorService to submit the query represented by this object
 * to a separate thread for execution. For such, a connection with the database is established here and
 * a prepared statement is created using the query string passed at construction.
 * <p>
 * The connection is closed after the query is executed and its result set processed as required.
 *
 * @author Enzo Bestetti (K230118872)
 * @version 2024.03.07
 */
public class Query {

    private final String queryString;
    private Connection conn;

    /**
     * Construct a Query object with a query string.
     *
     * @param queryString the SQL query to be executed in the database.
     */
    public Query(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            conn.close();
            // System.out.println("Connection to the database successfully closed.");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getCause());
        }
    }

    /**
     * Create a PreparedStatement object using the query string passed at construction.
     *
     * @return a PreparedStatement object that can be used to execute the query in the database.
     */
    public PreparedStatement getStatement() {
        openConnection();
        System.out.println("Connecting to the database...");
        try {
            return conn.prepareStatement(queryString);
        } catch (SQLException e) {
            System.out.println("Error creating prepared statement: " + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getCause());
            return null;
        }
    }

    /**
     * Open a connection to the database using a DatabaseConnector object.
     */
    private void openConnection() {
        conn = DatabaseConnector.connect();
    }
}



