package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.sql.queries.Query;
import utils.sql.queries.concurrent.QueryExecutor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class StatisticsController extends AbstractController {


    private AnchorPane parent;

    private LocalDate startDate, endDate;

    @FXML
    private Label statistic, title;

    /**
     * Constructor for the AbstractController class.
     */
    public StatisticsController() {
        this(null);
    }

    protected StatisticsController(String queryString) {
        super(queryString);
    }

    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/statistics-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
        } catch (IOException e) {
            System.out.println("Error loading the statistics frame! " + e.getCause() + e.getMessage() + e.getStackTrace());
        }

        setAverageTotalCasesLabel();

        Node a = parent;

        Stage stage = (Stage) a.getScene().getWindow();
    }

    public void initialiseDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Double calculateAverageTotalCases() {
        Query query = new Query("SELECT AVG(total_cases) AS average_total_cases FROM covid_london WHERE date BETWEEN '" + startDate + "' AND '" + endDate + "';");
        QueryExecutor executor = new QueryExecutor(query);

        ResultSet data;
        double ret = 0;

        try {
            data = executor.runQuery().get();
            data.next();
            ret = data.getDouble("average_total_cases");
        } catch (InterruptedException | ExecutionException | SQLException e) {
            System.out.println("Error generating result set! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }
        return ret;
    }

    private void setAverageTotalCasesLabel() {
        title.setText("Average Total Cases in London between " + startDate + " and " + endDate);

        Double displayValue = (double) Math.round(calculateAverageTotalCases() * 100) / 100;

        statistic.setText(displayValue + " (2d.p.)");
    }

    public void showStatisticsScreen() {
        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 960, 600));
        stage.show();
    }
}
