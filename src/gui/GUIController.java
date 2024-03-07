package gui;

import concurrent.QueryExecutor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sql.connector.DatabaseConnector;
import sql.queries.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Future;

@SuppressWarnings("all")
public class GUIController {

    private Future<ResultSet> queryResult;
    private Connection conn;
    private DatabaseConnector connector;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button queryButton, deriveStatsButton, nextDateButton, previousDateButton, nextRecordButton, previousRecordButton;
    @FXML
    private TextFlow genInfoFlow, gmrFlow, medFlow;

    private int indexCurrentlyShowing;
    private ArrayList<CovidData> data;

    public GUIController() {
        this.connector = new DatabaseConnector();
        this.indexCurrentlyShowing = 0;
    }

    public Pane beginLoading() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-app.fxml"));

        try {
            GridPane pane = fxmlLoader.load();
            return pane;
        } catch (Exception e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause());
        }
        return null;
    }

    public String formattedDate(LocalDate date) {
        if (date == null) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")).toString();
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")).toString();
    }

    @FXML
    private void queryButtonClicked() {
        Query testQuery = new Query("SELECT * FROM covid_london WHERE date = '" + formattedDate(datePicker.getValue()) + "' ORDER BY borough");
        QueryExecutor executor = new QueryExecutor(testQuery);
        try {
            // Thread.sleep(100); - We may need to introduce a delay here if the query is not fast enough.
            queryResult = executor.runQuery();
            ResultSet set = queryResult.get();
            processQuery(set);
            Thread.sleep(100); //ensures that the query is processed before the connection is closed
            testQuery.closeConnection();
        } catch (Exception e) {
            System.out.println("Error fetching result set: " + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getCause());
        }
    }

    @FXML
    private void nextDateButtonClicked() throws NullPointerException {
        datePicker.setValue(datePicker.getValue().plusDays(1));
        queryButtonClicked();
    }

    @FXML
    private void previousDateButtonClicked() throws NullPointerException {
        datePicker.setValue(datePicker.getValue().minusDays(1));
        queryButtonClicked();
    }

    @FXML
    private void nextRecordButtonClicked() throws NullPointerException {
        if (indexCurrentlyShowing == data.size() - 1) {
            indexCurrentlyShowing = -1;

        }

        Platform.runLater(() -> updateGUI(data.get(++indexCurrentlyShowing % data.size())));
    }

    @FXML
    private void previousRecordButtonClicked() throws NullPointerException {

        if (indexCurrentlyShowing == 0) {
            indexCurrentlyShowing = data.size();
        }

        Platform.runLater(() -> updateGUI(data.get(--indexCurrentlyShowing % data.size())));
    }

    private void processQuery(ResultSet set) throws SQLException {
        data = new ArrayList<>();
        while (set.next()) {
            data.add(new CovidData(set.getString("date"), set.getString("borough"), set.getInt("retail_and_recreation"),
                    set.getInt("grocery_and_pharmacy"), set.getInt("parks"), set.getInt("transit_stations"),
                    set.getInt("workplaces"), set.getInt("residential"), set.getInt("new_cases"),
                    set.getInt("total_cases"), set.getInt("new_deaths"), set.getInt("total_deaths")));
        }
        Platform.runLater(() -> updateGUI(data.get(indexCurrentlyShowing % data.size())));
    }

    private void updateGUI(CovidData data) {
        //Update general information
        genInfoFlow.getChildren().clear();
        genInfoFlow.getChildren().add(new Text("Borough: " + data.getBorough().toString() + "\n"));
        genInfoFlow.getChildren().add(new Text("Total Cases: " + data.getTotalCases() + "\n"));
        genInfoFlow.getChildren().add(new Text("Total Deaths: " + data.getTotalDeaths() + "\n"));

        //Update GMR information
        gmrFlow.getChildren().clear();
        gmrFlow.getChildren().add(new Text("Retail & Recreation: " + data.getRetailRecreationGMR() + "\n"));
        gmrFlow.getChildren().add(new Text("Grocery & Pharmacy: " + data.getGroceryPharmacyGMR() + "\n"));
        gmrFlow.getChildren().add(new Text("Parks: " + data.getParksGMR() + "\n"));
        gmrFlow.getChildren().add(new Text("Transit Stations: " + data.getTransitGMR() + "\n"));
        gmrFlow.getChildren().add(new Text("Workplaces: " + data.getWorkplacesGMR() + "\n"));
        gmrFlow.getChildren().add(new Text("Residential: " + data.getResidentialGMR() + "\n"));

        //Update medical information
        medFlow.getChildren().clear();
        medFlow.getChildren().add(new Text("New Cases: " + data.getNewCases() + "\n"));
        medFlow.getChildren().add(new Text("New Deaths: " + data.getNewDeaths()));

    }
}
