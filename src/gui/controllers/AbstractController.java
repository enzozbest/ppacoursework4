package gui.controllers;

import utils.sql.queries.Query;
import utils.sql.queries.QueryProcessor;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public abstract class AbstractController {

    protected Query query;
    protected QueryExecutor executor;
    protected QueryProcessor processor;

    protected ResultSet data;

    protected AbstractController(String queryString){
        Query query = new Query(queryString);
        data = queryDatabase(query);
    }

    private ResultSet queryDatabase(Query query){
        try {
            return new QueryExecutor(query).runQuery().get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error generating result set! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace() );
        }
        return null;
    }
}
