package utils.sql.queries.processors;

import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractProcessor {


    protected ResultSet resultSet;

    protected AbstractProcessor(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    protected abstract ObservableList processQuery() throws SQLException;

}