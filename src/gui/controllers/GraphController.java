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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import utils.sql.queries.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class is a controller for the graph scene. It is used to display the plot of the data that is passed to it.
 * <p>
 * The class extends the AbstractController class and uses the data that is passed to it to display the plot.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.28
 */
public class GraphController extends AbstractController {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ArrayList<String> boroughNames;
    private final ArrayList<String> statistics;
    private int numberGraphsDrawn;
    @FXML
    private Text title;
    @FXML
    private ImageView background, next, prev, small_character;
    @FXML
    private Text back_text, forward_text;
    @FXML
    private HBox graph_container;
    @FXML
    private ComboBox<String> combo_box;
    private int indexCurrentlyShowing;
    private AnchorPane parent;

    /**
     * The constructor for the PlotController class.
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

        indexCurrentlyShowing = 0;
        numberGraphsDrawn = 0;
    }

    /**
     * This method is used to run the initial query to populate the combo box with the borough names and the statistics
     * that can be displayed.
     */
    private void initialQuery() {
        Query boroughQuery = new Query("SELECT DISTINCT borough FROM covid_london ORDER BY borough;");
        Query statisticsQuery = new Query("SELECT DISTINCT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'covid_london';");

        super.runBackgroundTask(super.queryDatabase(boroughQuery), () -> {
            ResultSet boroughResultSet = data;
            try {
                while (boroughResultSet.next()) {
                    boroughNames.add(boroughResultSet.getString("borough"));
                }
            } catch (SQLException e) {
                System.out.println("Error Retrieving metadata from database! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            this.addListener();
            this.populateComboBoxBoroughs();
            this.drawLineGraph();
        });

        super.runBackgroundTask(super.queryDatabase(statisticsQuery), () -> {
            ResultSet statisticsResultSet = data;
            try {
                while (statisticsResultSet.next()) {
                    statistics.add(statisticsResultSet.getString("COLUMN_NAME"));
                }
                statistics.remove("date");
                statistics.remove("borough");
            } catch (SQLException e) {
                System.out.println("Error Retrieving metadata from database! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
            }

        });
    }

    /**
     * This method loads the FXML file for the graph scene and sets the controller for the file to this class.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../resources/fxml/graph-frame.fxml"));
        loader.setController(this);
        try {
            parent = loader.load();
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../resources/styles/default.css")).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading the FXML file for the graph-frame! \n" + e.getCause() + "\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

        this.initialQuery();

        this.setGraphPanel();

        scene = new Scene(parent, 960, 600);

    }

    /**
     * This method is used to set the panel for the graph scene.
     * <p>
     * This method sets the background, back button, forward button, next button, and previous button for the graph
     * scene. It also sets the navigation events for the scene.
     */
    private void setGraphPanel() {
        this.setBackground();
        this.setBackButton();
        this.setForwardButton();
        this.setNextButton();
        this.setPrevButton();
        this.setNavigationEvents(true);
    }

    /**
     * This method is used to add a listener to the combo box.
     * <p>
     * This method adds a listener to the combo box to listen for changes in the value of the combo box. When the value
     * of the combo box changes, the method will redraw the graph based on the value of the combo box.
     */
    private void addListener() {
        combo_box.valueProperty().addListener((observableValue, o, t1) -> {
            if (numberGraphsDrawn < 1 || t1 == null) {
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

    /**
     * Method to set the background.
     * <p>
     * This method sets the background for the graph scene by setting the image and size of the background.
     */
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

    /**
     * Method to draw the line graph.
     * <p>
     * This method is used to draw the line graph for the data that is passed to it.
     */
    private void drawLineGraph() {
        Plotter linePlotter = new LinePlotter(new CategoryAxis(), "Date", new NumberAxis(), "Total Deaths");
        title.setText("Currently Showing: Total Deaths over Time for " + combo_box.getValue());

        Query query = new Query("SELECT MONTH(`date`) AS `month`, YEAR(`date`) AS `year`, total_deaths " +
                "FROM covid_london " +
                "WHERE `date` BETWEEN '" + startDate + "' AND '" + endDate + "' AND borough = \"" + combo_box.getValue() + "\" GROUP BY MONTH(`date`), YEAR(`date`)" +
                "ORDER BY `date`;");

        super.runBackgroundTask(super.queryDatabase(query), () -> {
            graph_container.getChildren().clear();
            ResultSet resultSet = data;
            linePlotter.setData(resultSet);
            Chart lineChart = linePlotter.plot();
            lineChart.setPrefWidth(800);
            lineChart.setLegendVisible(false);

            graph_container.getChildren().add(lineChart);
            numberGraphsDrawn++;
        });
    }

    /**
     * Method to draw the bar chart.
     * <p>
     * This method is used to draw the bar chart for the data that is passed to it.
     */
    private void drawBarChart() {
        Plotter barPlotter = new BarChartPlotter(new CategoryAxis(), "Boroughs", new NumberAxis(), combo_box.getValue());
        title.setText(combo_box.getValue() + " across London");

        Query barChartQuery = new Query("SELECT AVG(" + combo_box.getValue() + ") AS average_statistic, borough FROM covid_london " +
                "WHERE `date` BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY borough ORDER BY borough;");

        super.runBackgroundTask(super.queryDatabase(barChartQuery), () -> {
            graph_container.getChildren().clear();
            ResultSet barChartData = data;
            barPlotter.setData(barChartData);
            Chart barChart = barPlotter.plot();
            barChart.setPrefWidth(800);
            barChart.setLegendVisible(false);

            graph_container.getChildren().add(barChart);
            numberGraphsDrawn++;
        });
    }

    /**
     * Method to populate the combo box with the borough names.
     * <p>
     * This method is used to populate the combo box with the borough names that are passed to it.
     */
    private void populateComboBoxBoroughs() {
        combo_box.getItems().clear();
        combo_box.getItems().addAll(boroughNames);
        combo_box.setValue("Barking And Dagenham");
    }

    /**
     * Method to populate the combo box with the statistics.
     * <p>
     * This method is used to populate the combo box with the statistics that are passed to it.
     */
    private void populateComboBoxStatistics() {
        combo_box.getItems().clear();
        combo_box.getItems().addAll(statistics);
        combo_box.setValue("retail_and_recreation");
    }

    /**
     * Method to set the navigation events.
     * <p>
     * This method is used to set the navigation events for the graph scene. It sets the navigation events for the back
     * button, forward button, and the text on the buttons.
     */
    private void setNavigationEvents(boolean setEventsFlag) {
        if (setEventsFlag) {
            super.setNavigationEvents(true, back_text, forward_text, "stats", "welcome");
            return;
        }
        super.setNavigationEvents(false, back_text, forward_text, "stats", "welcome");
    }
}
