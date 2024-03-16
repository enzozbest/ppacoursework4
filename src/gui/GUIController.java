package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sql.queries.Query;
import sql.queries.concurrent.QueryExecutor;
import sql.queries.processors.RecordGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * This class is the controller for the main application window.
 * It handles the user's interactions with the GUI and updates the GUI.
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.07
 */
@SuppressWarnings("all")
public class GUIController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button queryButton, deriveStatsButton, nextDateButton, previousDateButton, nextRecordButton, previousRecordButton;
    @FXML
    private TextFlow genInfoFlow, gmrFlow, medFlow;
    private Future<ResultSet> queryResult;
    private int indexCurrentlyShowing;
    private ArrayList<CovidData> data;

    /**
     * Constructor for the GUIController.
     * It initialises the DatabaseConnector and sets the indexCurrentlyShowing to 0.
     * The indexCurrentlyShowing is used to keep track of which CovidData object is currently being displayed.
     * The DatabaseConnector is used to connect to the database to execute queries.
     */
    public GUIController() {
        this.indexCurrentlyShowing = 0;
    }

    /**
     * This method is called when the GUI is being loaded.
     * It loads the main-app.fxml file.
     *
     * @return The GridPane that contains the main application window.
     */
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

    /**
     * This method is called when the user clicks the "Query Database" button.
     */
    @FXML
    private void queryButtonClicked() {
        ensureDateNotNull();
        queryDatabase("SELECT * FROM covid_london WHERE date = '" + datePicker.getValue() + "' ORDER BY borough");
        updateGUI(data.get(indexCurrentlyShowing % data.size()));
        //deriveStatsButtonClicked();
    }

    /**
     * Create a new Query object and process it on a separate thread.
     */
    private void queryDatabase(String queryString) {
        Query query = new Query(queryString);
        RecordGenerator processor = new RecordGenerator(new QueryExecutor(query));
        try {
            processor.executeQuery(); //Query the database
            data = processor.processQuery(); //Process the result set and create an ArrayList
            Thread.sleep(100); //Ensure query is fully processed before closing connection
        } catch (SQLException | InterruptedException e) {
            System.out.println("Error fetching result set: " + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getCause());
        } finally {
            query.closeConnection();
        }
    }

    /**
     * This method is called when the user clicks the "Next" button.
     * It increments the date by one day and calls the method that will run the query with the new date.
     */
    @FXML
    private void nextDateButtonClicked() {
        ensureDateNotNull();
        datePicker.setValue(datePicker.getValue().plusDays(1));
        queryDatabase("SELECT * FROM covid_london WHERE date = '" + datePicker.getValue() + "' ORDER BY borough");
        updateGUI(data.get(indexCurrentlyShowing % data.size()));
    }

    /**
     * This method is called when the user clicks the "Previous" button.
     * It decrements the date by one day and calls the method that will run the query with the new date.
     */
    @FXML
    private void previousDateButtonClicked() {
        ensureDateNotNull();
        datePicker.setValue(datePicker.getValue().minusDays(1));
        queryDatabase("SELECT * FROM covid_london WHERE date = '" + datePicker.getValue() + "' ORDER BY borough");
        updateGUI(data.get(indexCurrentlyShowing % data.size()));
    }

    /**
     * This method is called when the user clicks the "Next Record" button.
     * It updates the GUI with the data from the new CovidData object,
     * representing a different borough of London and its Covid-related data.
     *
     * @throws NullPointerException if the data list has not been initialised yet
     * @throws ArithmeticException  if the data list is empty.
     */
    @FXML
    private void nextRecordButtonClicked() throws NullPointerException, ArithmeticException {
        updateGUI(data.get((++indexCurrentlyShowing + data.size()) % data.size()));
    }

    /**
     * This method is called when the user clicks the "Previous Record" button.
     * It updates the GUI with the data from the new CovidData object,
     * representing a different borough of London and its Covid-related data.
     *
     * @throws NullPointerException if the data list has not been initialised yet.
     * @throws ArithmeticException  if the data list is empty.
     */
    @FXML
    private void previousRecordButtonClicked() throws NullPointerException, ArithmeticException {
        updateGUI(data.get((--indexCurrentlyShowing + data.size()) % data.size()));
    }

    @FXML
    private void deriveStatsButtonClicked() {
        Query query = new Query("SELECT sum(total_cases) as total_cases FROM covid_london WHERE date = '" + datePicker.getValue() + "'");
        QueryExecutor executor = new QueryExecutor(query);
        try {
            queryResult = executor.runQuery();
            ResultSet set = queryResult.get();
            while (set.next()) {
                System.out.println("Total cases: " + set.getInt("total_cases"));
            }
            query.closeConnection();
        } catch (Exception e) {
            System.out.println("Error fetching result set: " + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getCause());
        }
    }

    /**
     * This method is called to update the GUI with the data from a CovidData object.
     * It updates the general information, the Google Mobility Report information and the medical information.
     * It also updates the indexCurrentlyShowing to keep track of which CovidData object is currently being displayed.
     * This is necessary because the method is called from a different thread.
     *
     * @param data The CovidData object to be displayed in the GUI.
     */
    private void updateGUI(CovidData data) {
        updateGeneralInformation(data);
        updateGMRInformation(data);
        updateMedicalInformation(data);
        indexCurrentlyShowing = this.data.indexOf(data);
    }

    /**
     * This method is called to update the GUI with the general information from a CovidData object.
     * It updates the TextFlow with the borough, total cases and total deaths.
     *
     * @param data The CovidData object to be displayed in the GUI.
     */
    private void updateGeneralInformation(CovidData data) {
        genInfoFlow.getChildren().clear();
        genInfoFlow.getChildren().add(new Text("Borough: " + data.borough() + "\n"));
        genInfoFlow.getChildren().add(new Text("Total Cases: " + data.totalCases() + "\n"));
        delayUpdate(10);
        genInfoFlow.getChildren().add(new Text("Total Deaths: " + data.totalDeaths() + "\n"));
    }

    /**
     * This method is called to update the GUI with the Google Mobility Report information from a CovidData object.
     * It updates the TextFlow with the GMR information for each category.
     *
     * @param data The CovidData object to be displayed in the GUI.
     */
    private void updateGMRInformation(CovidData data) {
        gmrFlow.getChildren().clear();
        gmrFlow.getChildren().add(new Text("Retail & Recreation: " + data.retailAndRecreation() + "\n"));
        gmrFlow.getChildren().add(new Text("Grocery & Pharmacy: " + data.groceryAndPharmacy() + "\n"));
        gmrFlow.getChildren().add(new Text("Parks: " + data.parks() + "\n"));
        gmrFlow.getChildren().add(new Text("Transit Stations: " + data.transitStations() + "\n"));
        gmrFlow.getChildren().add(new Text("Workplaces: " + data.workplaces() + "\n"));
        gmrFlow.getChildren().add(new Text("Residential: " + data.residential() + "\n"));
    }

    /**
     * This method is called to update the GUI with the medical information from a CovidData object.
     * It updates the TextFlow with the new cases and new deaths.
     *
     * @param data The CovidData object to be displayed in the GUI.
     */
    private void updateMedicalInformation(CovidData data) {
        medFlow.getChildren().clear();
        medFlow.getChildren().add(new Text("New Cases: " + data.newCases() + "\n"));
        medFlow.getChildren().add(new Text("New Deaths: " + data.newDeaths()));
    }

    private void delayUpdate(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ensureDateNotNull() {
        if (datePicker.getValue() == null) {
            datePicker.setValue(LocalDate.now());
        }
    }
}
