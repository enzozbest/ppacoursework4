package gui.controllers;

import gui.components.BarChartPlotter;
import gui.components.LinePlotter;
import gui.components.Plotter;
import gui.components.ScatterPlotter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import utils.CovidData;
import utils.sql.queries.Query;
import utils.sql.queries.concurrent.QueryExecutor;
import utils.sql.queries.processors.RecordGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * This class is the controller for the control panel window. This window displays general control structures that are
 * used to test the functionality of the application. It is not the final version of the application's GUI, but it is
 * used to test the functionality of the application's backend and the database connection.
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.18
 */
@SuppressWarnings("all")
public class ControlPanelController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button queryButton, lineButton, nextDateButton, previousDateButton, nextRecordButton, previousRecordButton;
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
    public ControlPanelController() {
        this.indexCurrentlyShowing = 0;
    }

    /**
     * This method is called when the GUI is being loaded.
     * It loads the control-panel.fxml file.
     *
     * @return The GridPane that contains the main application window.
     */
    public Pane beginLoading() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/control-panel.fxml"));

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

    /**
     * This method is called when the user clicks the "Line Chart" button.
     * It creates a new PlotController and a new LinePlotter to display the data in a line chart.
     * The line chart will display the total cases for the borough over time.
     * The data will be displayed for the last 180 days before and after the date selected by the user.
     * The line chart will be displayed in a new window.
     */
    @FXML
    private void lineButtonClicked() {
        PlotController lineController = new PlotController("SELECT `date`, MONTH(`date`) AS month, total_cases, "
                + "borough FROM covid_london WHERE borough='"
                + data.get(indexCurrentlyShowing).borough() + "' "
                + "AND `date` BETWEEN '" + datePicker.getValue().minusDays(180) + "' AND '"
                + datePicker.getValue().plusDays(180) + "' GROUP BY MONTH(`date`) ORDER BY `date`;");

        Plotter linePlotter = new LinePlotter("Greenwich over Time", new CategoryAxis(), "Date",
                new NumberAxis(), "Total cases");

        lineController.showGraph(linePlotter);
    }

    /**
     * This method is called when the user clicks the "Bar Chart" button.
     * It creates a new PlotController and a new BarChartPlotter to display the data in a bar chart.
     * The bar chart will display the total cases for each borough on the date selected by the user.
     * The bar chart will be displayed in a new window.
     */
    @FXML
    private void barChartButtonClicked() {
        PlotController bcController = new PlotController("SELECT borough, total_cases FROM covid_london WHERE date='" + datePicker.getValue() + "' GROUP BY borough;");

        Plotter bcPlotter = new BarChartPlotter("Boroughs on this day", new CategoryAxis(), "Boroughs", new NumberAxis(), "Number of Cases");

        bcController.showGraph(bcPlotter);
    }

    /**
     * This method is called when the user clicks the "Scatter Chart" button.
     * It creates a new PlotController and a new ScatterPlotter to display the data in a scatter chart.
     * The scatter chart will display the total cases for the borough over time.
     * The data will be displayed for the last 180 days before and after the date selected by the user.
     * The scatter chart will be displayed in a new window.
     */
    @FXML
    private void scatterButtonClicked() {
        PlotController plotController = new PlotController("SELECT `date`, total_cases FROM covid_london WHERE borough ='" + data.get(indexCurrentlyShowing).borough() + "' AND `date` BETWEEN '" + datePicker.getValue().minusDays(180) + "' AND '" + datePicker.getValue().plusDays(180) + "' ORDER BY `date`;");
        Plotter sPlotter = new ScatterPlotter("CV-19 Boroughs", new CategoryAxis(), "Boroughs", new NumberAxis(), "Number of Cases");
        sPlotter.setData(plotController.getData());

        plotController.showGraph(sPlotter);

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

    /**
     * Introduce a delay by sleeping the thread for a given number of milliseconds.
     *
     * @param millis The number of milliseconds to sleep the thread for.
     */
    private void delayUpdate(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure that the date picker does not return a null value.
     */
    private void ensureDateNotNull() {
        if (datePicker.getValue() == null) {
            datePicker.setValue(LocalDate.now());
        }
    }
}
