package gui.controllers;

import gui.components.AssetLoader;
import gui.components.BarChartPlotter;
import gui.components.LinePlotter;
import gui.components.Plotter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import utils.sql.queries.Query;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * This class is a controller for the plot-frame.fxml file. It is used to display the plot of the data
 * that is passed to it.
 * <p>
 * The class extends the AbstractController class and uses the data that is passed to it to display the plot
 * in a new window.
 * <p>
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.28
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GraphController extends AbstractController {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ArrayList<String> boroughNames;
    private final ArrayList<String> statistics;
    private int numberGraphsDrawn;
    @FXML
    private Label title;
    @FXML
    private ImageView background, next, prev, small_character;
    @FXML
    private Text back_text, forward_text;
    @FXML
    private HBox graph_container;
    @FXML
    private ComboBox combo_box;
    private int indexCurrentlyShowing;
    private AnchorPane parent;

    /**
     * The constructor for the PlotController class. It takes a string as a parameter and passes it to the
     * super class constructor.
     *
     * @param startDate the start date for the data to be displayed.
     * @param endDate   the end date for the data to be displayed.
     */
    public GraphController(LocalDate startDate, LocalDate endDate) {
        super(null);

        this.startDate = startDate;
        this.endDate = endDate;
        this.boroughNames = new ArrayList<>();
        this.statistics = new ArrayList<>();
        this.initialQuery();

        indexCurrentlyShowing = 0;
        numberGraphsDrawn = 0;
    }

    private void initialQuery() {
        Query boroughQuery = new Query("SELECT DISTINCT borough FROM covid_london ORDER BY borough;");
        Query statisticsQuery = new Query("SELECT DISTINCT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'covid_london';");

        QueryExecutor boroughExecutor = new QueryExecutor(boroughQuery);
        QueryExecutor statisticsExecutor = new QueryExecutor(statisticsQuery);

        try {

            ResultSet boroughResultSet = boroughExecutor.runQuery().get();
            ResultSet statisticsResultSet = statisticsExecutor.runQuery().get();

            while (boroughResultSet.next()) {
                boroughNames.add(boroughResultSet.getString("borough"));
            }

            while (statisticsResultSet.next()) {
                statistics.add(statisticsResultSet.getString("COLUMN_NAME"));
            }
            statistics.remove("date");
            statistics.remove("borough");

        } catch (SQLException | InterruptedException | ExecutionException e) {
            System.out.println("Error Retrieving metadata from database! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * This method loads the FXML file for the plot-frame and sets the controller for the file to this class.
     *
     * @return the parent node of the FXML file.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/graph-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../resources/styles/default.css")).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading the FXML file for the graph-frame! \n" + e.getCause() + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

        scene = new Scene(parent, 960, 600);

        this.setGraphPanel();
    }

    private void setGraphPanel() {
        this.setBackground();
        this.setBackButton();
        this.setForwardButton();
        this.setNextButton();
        this.setPrevButton();
        this.setNavigationEvents(true);
        this.addListener();
        this.populateComboBoxBoroughs();
        this.drawLineGraph();
    }

    private void addListener() {
        combo_box.valueProperty().addListener((observableValue, o, t1) -> {
            if (numberGraphsDrawn < 1) {
                return;
            }
            graph_container.getChildren().clear();
            if (boroughNames.contains(t1)) {
                drawLineGraph();
                return;
            }
            drawBarChart();
        });
    }

    private void setBackground() {
        background.setImage(AssetLoader.GRAPH_BACKGROUND);
        background.setFitWidth(960);
        background.setFitHeight(600);
        background.setPreserveRatio(true);
    }

    /**
     * Method to set the forward button.
     * <p>
     * This method sets the forward button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the forward button to ensure correct styling.
     */
    private void setForwardButton() {
        forward_text.getStyleClass().add("clickable");
        super.hoverFlash(forward_text);
    }

    /**
     * Method to set the back button.
     * <p>
     * This method sets the back button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the back button to ensure correct styling.
     */
    private void setBackButton() {
        back_text.getStyleClass().add("clickable");
        super.hoverFlash(back_text);
    }

    /**
     * Method to set the next button.
     * <p>
     * This method sets the next button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the next button to ensure correct styling.
     */
    private void setNextButton() {
        next.setImage(AssetLoader.ARROW);
        next.setFitWidth(110);
        next.setFitHeight(40);
        next.setPreserveRatio(true);
        next.getStyleClass().add("clickable");

        next.setOnMouseClicked(event -> {
            graph_container.getChildren().clear();
            if (indexCurrentlyShowing == 0) {
                indexCurrentlyShowing = 1;
                populateComboBoxStatistics();
                combo_box.setValue("retail_and_recreation");
                return;
            }
            indexCurrentlyShowing = 0;
            populateComboBoxBoroughs();
            combo_box.setValue("Barking And Dagenham");
        });

    }

    /**
     * Method to set the previous button.
     * <p>
     * This method sets the previous button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the previous button to ensure correct styling.
     */
    private void setPrevButton() {
        prev.setImage(AssetLoader.ARROW);
        prev.setScaleX(-1);
        prev.setFitWidth(110);
        prev.setFitHeight(40);
        prev.setPreserveRatio(true);
        prev.getStyleClass().add("clickable");

        prev.setOnMouseClicked(event -> {
            graph_container.getChildren().clear();
            if (indexCurrentlyShowing == 0) {
                indexCurrentlyShowing = 1;
                populateComboBoxStatistics();
                combo_box.setValue("retail_and_recreation");
                return;
            }
            indexCurrentlyShowing = 0;
            populateComboBoxBoroughs();
            combo_box.setValue("Barking And Dagenham");
        });
    }

    private void drawLineGraph() {
        Plotter linePlotter = new LinePlotter("Total Deaths x Time", new CategoryAxis(), "Date", new NumberAxis(), "Total Deaths");
        title.setText("Currently Showing: Total Deaths over Time for " + combo_box.getValue());

        Query query = new Query("SELECT MONTH(`date`) AS `month`, YEAR(`date`) AS `year`, total_deaths " +
                "FROM covid_london " +
                "WHERE `date` BETWEEN '" + startDate + "' AND '" + endDate + "' AND borough = \"" + combo_box.getValue() + "\" GROUP BY MONTH(`date`), YEAR(`date`)" +
                "ORDER BY `date`;");

        QueryExecutor executor = new QueryExecutor(query);


        ResultSet resultSet = null;
        try {
            resultSet = executor.runQuery().get();
        } catch (InterruptedException | ExecutionException e) {
        }
        linePlotter.setData(resultSet);


        Chart lineChart = linePlotter.plot();
        lineChart.setPrefWidth(800);
        lineChart.setLegendVisible(false);

        graph_container.getChildren().add(lineChart);

        numberGraphsDrawn++;
    }

    private void drawBarChart() {
        indexCurrentlyShowing = 1;

        if (!combo_box.getItems().contains("retail_and_recreation")) {
            populateComboBoxStatistics();
        }

        if (combo_box.getValue() == null) {
            combo_box.setValue("retail_and_recreation");
        }

        Plotter barPlotter = new BarChartPlotter(combo_box.getValue() + " across London", new CategoryAxis(), "Boroughs", new NumberAxis(), combo_box.getValue().toString());
        title.setText(combo_box.getValue() + " across London");

        Query barChartQuery = new Query("SELECT AVG(" + combo_box.getValue() + ") AS average_statistic, borough FROM covid_london " +
                "WHERE `date` BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY borough ORDER BY borough;");

        QueryExecutor queryExecutor = new QueryExecutor(barChartQuery);

        try {
            ResultSet barChartData = queryExecutor.runQuery().get();
            barPlotter.setData(barChartData);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error plotting bar chart" + e.getStackTrace() + e.getCause() + e.getMessage());
        }

        Chart barChart = barPlotter.plot();
        barChart.setPrefWidth(800);
        barChart.setLegendVisible(false);

        graph_container.getChildren().add(barChart);

        numberGraphsDrawn++;
    }

    private void populateComboBoxBoroughs() {
        combo_box.getItems().clear();
        combo_box.getItems().addAll(boroughNames);
        combo_box.setValue("Barking And Dagenham");
    }

    private void populateComboBoxStatistics() {
        combo_box.getItems().clear();
        combo_box.getItems().addAll(statistics);
        combo_box.setValue("retail_and_recreation");
    }

    private void setNavigationEvents(boolean setting) {
        if (setting) {
            super.setNavigationEvents(true, back_text, forward_text, "stats", "welcome");
            return;
        }
        super.setNavigationEvents(false, back_text, forward_text, "stats", "welcome");
    }
}
