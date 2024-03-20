package gui.controllers;

import utils.sql.queries.Query;
import utils.sql.queries.QueryProcessor;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class to be extended by all controllers in the application.
 * <p>
 * This class provides a common interface for all controllers to query the database and process the results.
 * It also provides a common interface for all controllers to handle the results of the query.
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
 */
public abstract class AbstractController {

    protected Query query;
    protected QueryExecutor executor;
    protected QueryProcessor processor;

    protected ResultSet data;

    /**
     * Constructor for the AbstractController class.
     *
     * @param queryString the query string to be executed.
     */
    protected AbstractController(String queryString) {

        if (queryString == null) {
            return;
        }

        Query query = new Query(queryString);
        data = queryDatabase(query);
    }

    public abstract void beginLoading();

    /**
     * Method to query the database.
     */
    private ResultSet queryDatabase(Query query) {

        ResultSet ret;
        try {
            ret = new QueryExecutor(query).runQuery().get();
            return ret;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error generating result set! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }
        return null;
    }
}
