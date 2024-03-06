import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import sql.connector.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("all")
public class GUIController {

    private final MainApplication mainApplication;
    private Connection conn;
    private DatabaseConnector connector;
    @FXML
    private Label testLabel;

    public GUIController(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
        this.connector = new DatabaseConnector();
    }

    public BorderPane beginLoading() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        fxmlLoader.setController(this);

        try {
            BorderPane pane = fxmlLoader.load();
            testQuery();
            return pane;
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause());
        }
        return null;
    }

    public void testQuery() {
        this.conn = this.connector.connect();
        try (Statement s = conn.createStatement()) {

            ResultSet set = s.executeQuery("SELECT total_cases FROM covid_london WHERE borough='Haringey' AND date='15/10/2022'");
            set.next();
            int totalCases = set.getInt("total_cases");
            testLabel.setText(String.valueOf(totalCases));

        } catch (SQLException e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause() + "\n Could not execute query.");
        }
    }

}
