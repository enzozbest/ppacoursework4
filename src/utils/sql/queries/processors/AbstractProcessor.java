package utils.sql.queries.processors;

import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class AbstractProcessor {


    protected QueryExecutor executor;
    protected ResultSet resultSet;

    protected AbstractProcessor(QueryExecutor executor) {
        this.executor = executor;
    }

    protected abstract ArrayList processQuery() throws SQLException;

    /**
     * This method is called to execute the SQL query represented by the Query object passed at construction.
     * It uses the QueryExecutor object to run the query in a separate thread and returns a Future object representing
     * the ResultSet of the query.
     */
    public void executeQuery() {
        try {
            Future<ResultSet> queryResult = executor.runQuery();
            resultSet = queryResult.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error executing query (RecordGenerator class) " + Arrays.toString(e.getStackTrace()) + e.getCause() + e.getMessage());
        }
    }
}