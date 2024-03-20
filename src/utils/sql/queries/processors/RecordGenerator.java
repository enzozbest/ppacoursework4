package utils.sql.queries.processors;

import utils.CovidData;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecordGenerator extends AbstractProcessor {

    public RecordGenerator(QueryExecutor executor) {
        super(executor);
    }

    /**
     * This method is called to convert the results of a SQL query into a convenient format.
     * It creates a new CovidData record object for each row in the result set and adds it to the data list.
     *
     * @throws SQLException if the result set is empty or if there is an error accessing the data in the result set.
     */
    public ArrayList<CovidData> processQuery() throws SQLException {
        ArrayList<CovidData> data = new ArrayList<>();
        while (resultSet.next()) {
            data.add(new CovidData(resultSet.getString("date"), resultSet.getString("borough"), resultSet.getInt("retail_and_recreation"),
                    resultSet.getInt("grocery_and_pharmacy"), resultSet.getInt("parks"), resultSet.getInt("transit_stations"),
                    resultSet.getInt("workplaces"), resultSet.getInt("residential"), resultSet.getInt("new_cases"),
                    resultSet.getInt("total_cases"), resultSet.getInt("new_deaths"), resultSet.getInt("total_deaths")));
        }
        return data;
    }
}