package sql.queries;

import concurrent.QueryExecutor;
import gui.CovidData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class QueryProcessor {

    private final QueryExecutor executor;
    private ResultSet resultSet;
    private Future<ResultSet> queryResult;
    private ArrayList<CovidData> data;


    public QueryProcessor(QueryExecutor executor) {
        this.executor = executor;
    }

    public void executeQuery() {
        try {
            queryResult = executor.runQuery();
            resultSet = queryResult.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called to convert the results of a SQL query into a convenient format.
     * It creates a new CovidData object for each row in the result set and adds it to the data list.
     * It then calls the method that updates the GUI with the first CovidData object in the list for viewing.
     *
     * @throws NullPointerException if the data list has not been initialised yet or it is empty.
     * @throws SQLException         if there is an error processing the SQL result set.
     */
    public ArrayList<CovidData> processQuery() throws Exception {
        data = new ArrayList<>();
        while (resultSet.next()) {
            data.add(new CovidData(resultSet.getString("date"), resultSet.getString("borough"), resultSet.getInt("retail_and_recreation"),
                    resultSet.getInt("grocery_and_pharmacy"), resultSet.getInt("parks"), resultSet.getInt("transit_stations"),
                    resultSet.getInt("workplaces"), resultSet.getInt("residential"), resultSet.getInt("new_cases"),
                    resultSet.getInt("total_cases"), resultSet.getInt("new_deaths"), resultSet.getInt("total_deaths")));
        }
        return data;
    }

}
