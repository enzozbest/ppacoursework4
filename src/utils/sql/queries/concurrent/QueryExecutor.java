package utils.sql.queries.concurrent;

import utils.sql.queries.Query;

import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is responsible for executing a SQL query on a separate thread.
 * It uses a single thread executor to run the query and return a Future object representing the ResultSet of the query.
 * A separate thread is used to prevent the GUI from freezing while the query is being executed.
 * The runQuery() method retrieves the PreparedStatement from the Query object and executes it in a separate thread. It
 * returns a future object representing the ResultSet of the query.
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.07
 */
public class QueryExecutor {


    private final ExecutorService threadPoolExecutor;
    private final Query query;

    /**
     * Constructor for the QueryExecutor class.
     *
     * @param query The query to be executed.
     */
    public QueryExecutor(Query query) {
        this.threadPoolExecutor = Executors.newSingleThreadExecutor();
        this.query = query;
    }

    /**
     * Executes the query in a separate thread and returns a Future object representing the ResultSet of the query.
     *
     * @return A Future object representing the ResultSet of the query.
     */
    public Future<ResultSet> runQuery() {
        return threadPoolExecutor.submit(() -> query.getStatement().executeQuery());
    }
}