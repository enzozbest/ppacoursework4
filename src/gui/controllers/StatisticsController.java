package gui.controllers;

import gui.components.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller class for the Statistics screen.
 * <p>
 * This class provides the functionality for the Statistics screen. It retrieves the statistics from the database and
 * displays them to the user. The statistics are displayed in the following order:
 * 1. Average Retail and Recreation GMR
 * 2. Average Workplace GMR
 * 3. Deaths in the Period
 * 4. Average Total Cases
 * <p>
 * The user can navigate between the statistics using the next and previous buttons.
 * The user can also return to the map frame using the back button, as well as proceed forward to the Graphs screen using
 * the forward button.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
 */
@SuppressWarnings("SqlResolve, SqlNoDataSourceInspection, ConstantConditions, Duplicates, unused")
public class StatisticsController extends AbstractController {

    @FXML
    private Label statistic, title, subtitle;
    @FXML
    private ImageView background, next, prev, small_character;
    @FXML
    private Text back_text, forward_text;
    private ArrayList<Double> statistics;
    private AnchorPane parent;
    private LocalDate startDate, endDate;
    private int currentStatistic;

    /**
     * No-argument Constructor for the AbstractController class.
     */
    public StatisticsController() {
    }

    /**
     * Constructor for the StatisticsController class.
     *
     * @param startDate The start date of the date range
     * @param endDate   The end date of the date range
     */
    public StatisticsController(LocalDate startDate, LocalDate endDate) {
        super("SELECT avg(retail_and_recreation) AS avg_ret_rec, avg(workplaces) AS avg_wp, sum(new_deaths) AS period_deaths, avg(total_cases) AS avg_period_cases " +
                "FROM covid_london " +
                "WHERE date BETWEEN '" + startDate + "'  AND '" + endDate + "'");
        this.startDate = startDate;
        this.endDate = endDate;
        statistics = new ArrayList<>();
        currentStatistic = 0;
    }

    /**
     * Method to begin loading the statistics frame.
     * <p>
     * This method loads the statistics frame and sets the background, forward and back buttons, and the statistics panel.
     * It also sets the mouse events for the forward and back buttons, allowing the user to change between different scenes
     * and between different statistics among those available.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/statistics-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
            parent.getStylesheets().add(getClass().getResource("../../resources/styles/default.css").toExternalForm());
        } catch (IOException e) {
            System.out.println("Error loading the statistics frame! " + e.getCause() + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }

        statistics = retrieveStatistics();

        this.setStatsPanel();

        scene = new Scene(parent, 960, 600);

        this.displayNextStatistic();
    }

    /**
     * Method to retrieve the statistics from the database.
     *
     * @return An ArrayList of Doubles containing the statistics retrieved from the database
     */
    private ArrayList<Double> retrieveStatistics() {
        ArrayList<Double> ret = new ArrayList<>();
        try {
            data.next();
            for (int i = 1; i <= 4; i++) {
                ret.add(data.getDouble(i));
            }
        } catch (SQLException e) {
            System.out.println("Error generating result set! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return ret;
    }

    /**
     * Method to set the statistics panel.
     * <p>
     * This method sets the statistics panel by adding the title, subtitle, and statistic to the panel. It also sets the
     * font for the title, subtitle, and statistic to the EpiQuest font.
     */
    private void setStatsPanel() {
        this.setBackground();
        this.setForwardButton();
        this.setBackButton();
        this.setNextButton();
        this.setPrevButton();
        this.setSmallCharacter();
        this.setMouseEvents(true);
    }

    /**
     * Method to display the next statistic.
     * <p>
     * This method displays the next statistic in the list of statistics available. The statistics are displayed in the
     * following order:
     * 1. Average Retail and Recreation GMR
     * 2. Average Workplace GMR
     * 3. Deaths in the Period
     * 4. Average Total Cases
     * <p>
     * The method wraps around the list of statistics, so that when the last statistic is displayed, the first statistic
     * is displayed next.
     */
    private void displayNextStatistic() {
        int wrappedIndex = (currentStatistic + 4) % 4;

        switch (wrappedIndex) {
            case 0 -> this.displayAverageRetailAndRecreation();
            case 1 -> this.displayAverageWorkplace();
            case 2 -> this.displayPeriodDeaths();
            case 3 -> this.displayAverageTotalCases();
        }
    }

    /**
     * Method to display the average retail and recreation GMR.
     * <p>
     * This method displays the average retail and recreation GMR in the date range specified by the user. The average
     * retail and recreation GMR is displayed to two decimal places.
     */
    private void displayAverageRetailAndRecreation() {
        title.setText("Average Retail and Recreation GMR");
        subtitle.setText("London: " + startDate + " to " + endDate);

        Double displayValue = (double) Math.round(statistics.getFirst() * 100) / 100;
        statistic.setText(displayValue + " (2d.p.)");
    }

    /**
     * Method to display the average workplace GMR.
     * This method displays the average workplaces GMR in the date range specified by the user. The average workplaces
     * GMR is displayed to two decimal places.
     */
    private void displayAverageWorkplace() {
        title.setText("Average Workplace GMR");
        subtitle.setText("London: " + startDate + " to " + endDate);

        Double displayValue = (double) Math.round(statistics.get(1) * 100) / 100;
        statistic.setText(displayValue + " (2d.p.)");
    }

    /**
     * Method to display the deaths in the period.
     * <p>
     * This method displays the deaths in the period specified by the user. The deaths in the period are displayed to two
     * decimal places.
     */
    private void displayPeriodDeaths() {
        title.setText("Deaths in the Period");
        subtitle.setText("London: " + startDate + " to " + endDate);

        Double displayValue = (double) Math.round(statistics.get(2) * 100) / 100;
        statistic.setText(displayValue + " (2d.p.)");
    }

    /**
     * Method to display the average total cases.
     * <p>
     * This method displays the average total cases in the date range specified by the user. The average total cases are
     * displayed to two decimal places.
     */
    private void displayAverageTotalCases() {
        title.setText("Average Total Cases");
        subtitle.setText("London: " + startDate + " to " + endDate);

        Double displayValue = (double) Math.round(statistics.get(3) * 100) / 100;
        statistic.setText(displayValue + " (2d.p.)");
    }

    /**
     * Method to set the background image.
     */
    private void setBackground() {
        background.setImage(AssetLoader.STATS_BACKGROUND);
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
    }

    private void setSmallCharacter() {
        small_character.setImage(AssetLoader.SMALL_CHARACTER);
        small_character.setFitWidth(275);
        small_character.setFitHeight(264);
        small_character.setPreserveRatio(true);
    }

    /**
     * Method to set the mouse events for the next and previous buttons.
     * <p>
     * This method sets the mouse events for the next and previous buttons by adding or removing the specified events
     * from the buttons. This is used to change the behaviour of the buttons when the user is navigating between
     * different statistics.
     *
     * @param setting Whether to add or remove the mouse events
     */
    private void setStatsEvents(boolean setting) {
        if (setting) {
            next.setOnMouseClicked(mouseEvent -> {
                currentStatistic++;
                displayNextStatistic();
            });
            prev.setOnMouseClicked(mouseEvent -> {
                currentStatistic--;
                displayNextStatistic();
            });
            return;
        }
        next.setOnMouseClicked(mouseEvent -> {
        });
        prev.setOnMouseClicked(mouseEvent -> {
        });
    }

    /**
     * Method to set the mouse events for the subtitle.
     * <p>
     * This method sets the mouse events for the subtitle by adding or removing the specified events from the subtitle.
     * This is used to change the behaviour of the subtitle when the date range is valid or invalid.
     *
     * @param setting Whether to add or remove the mouse events
     */
    private void setMouseEvents(boolean setting) {
        if (setting) {
            this.setStatsEvents(true);
            super.setNavigationEvents(true, back_text, forward_text, "map", "graph");
            return;
        }
        this.setStatsEvents(false);
        super.setNavigationEvents(false, back_text, forward_text, "map", "graph");
    }
}
