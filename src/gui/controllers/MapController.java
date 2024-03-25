package gui.controllers;

import gui.components.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings("rawtypes")
public class MapController extends AbstractController {

    @FXML
    private ListView list;
    @FXML
    private ImageView backText, mapFrame, mapImage, forwardText;
    @FXML
    private WebView webViewMap;
    private AnchorPane parent;

    /**
     * No-argument constructor for the MapController class
     */
    public MapController() {
    }

    /**
     * Constructor for the MapController class
     *
     * @param startDate The start date of the date range
     * @param endDate   The end date of the date range
     */
    public MapController(LocalDate startDate, LocalDate endDate) {
        super("SELECT borough, sum(new_deaths) AS borough_deaths " +
                "FROM covid_london " +
                "WHERE date BETWEEN '" + startDate + "'  AND '" + endDate + "' GROUP BY borough");
    }

    /**
     * Method to begin loading the map screen.
     * <p>
     * This method loads the map screen by setting the map panel.
     * The map panel is then displayed to the user.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/map-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
            parent.getStylesheets().add(getClass().getResource("../styles/default.css").toExternalForm());
        } catch (IOException e) {
            System.out.println("Error loading the map frame!" + e.getMessage() + e.getCause() + e.getStackTrace() + e.getLocalizedMessage());
        }
        setMapPanel();
        scene = new Scene(parent, 960, 600);
    }

    /**
     * Method to set the map panel.
     * <p>
     * This method sets the map panel by setting the background, map, back button, forward button, and borough colours.
     * The map panel is then displayed to the user.
     */
    private void setMapPanel() {
        setBackround();
        setMap();
        setBackButton();
        setForwardButton();
        setBoroughColours();
        setMouseEvents(true);
    }

    /**
     * Method to set the background of the map.
     * <p>
     * This method sets the background of the map by setting the image of the map frame to the map frame image.
     * The image is then resized to fit the dimensions of the map frame, and the preserve ratio is set to true to
     * ensure the image is not distorted.
     */
    private void setBackround() {
        mapFrame.setImage(AssetLoader.MAP_FRAME);
        mapFrame.setFitWidth(960);
        mapFrame.setFitHeight(600);
        mapFrame.setPreserveRatio(true);
    }

    /**
     * Method to set the map.
     * <p>
     * This method sets the map by setting the image of the map to the map image. The image is then resized to fit
     * the dimensions of the map frame, and the preserve ratio is set to true to ensure the image is not distorted.
     */
    private void setMap() {
        mapImage.setImage(AssetLoader.MAP);
        mapImage.setFitWidth(450);
        mapImage.setFitHeight(380);
        mapImage.setPreserveRatio(true);
    }

    /**
     * Method to set the back button.
     * <p>
     * This method sets the back button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the back button. to ensure correct styling.
     */
    private void setBackButton() {
        backText.setImage(AssetLoader.BACK);
        backText.setFitWidth(110);
        backText.setFitHeight(40);
        backText.setPreserveRatio(true);
        backText.getStyleClass().add("clickable");
    }

    /**
     * Method to set the forward button.
     * <p>
     * This method sets the forward button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the forward button. to ensure correct styling.
     */
    private void setForwardButton() {
        forwardText.setImage(AssetLoader.FORWARD);
        forwardText.setFitWidth(110);
        forwardText.setFitHeight(40);
        forwardText.setPreserveRatio(true);
        forwardText.getStyleClass().add("clickable");
    }

    /**
     * Method to set the mouse events for the forward and back buttons.
     *
     * @param setting Whether to add or remove the mouse events
     */
    private void setMouseEvents(boolean setting) {
        if (setting) {
            super.setNavigationEvents(true, backText, forwardText, "welcome", "stats");
            return;
        }
        super.setNavigationEvents(false, backText, forwardText, "welcome", "stats");
    }

    /**
     * Method to set the colours of the boroughs on the map.
     * <p>
     * This method sets the colours of the boroughs on the map based on the number of deaths in each borough.
     * The colours are set based on the number of deaths in each borough, with the following colour scheme:
     * 0 deaths: green
     * 1 death: yellow
     * 2 deaths: orange
     * 3 deaths: red
     * 4+ deaths: dark red
     */
    private void setBoroughColours() {
        try {
            while (data.next()) {
                int deaths = data.getInt("borough_deaths");
                String colour = findColour(deaths);
                Polygon polygon = new Polygon();
                polygon.getPoints().add(6.0);
                polygon.setId("borough-" + data.getString("borough"));
                polygon.setStyle("-fx-fill: " + colour);

                parent.getChildren().add(polygon);
                parent.getChildren().getLast().toFront();
            }
        } catch (SQLException e) {
            System.out.println("Error setting borough colours! " + e.getMessage() + e.getCause() + e.getStackTrace());
        }
    }

    /**
     * Method to find the colour for the borough based on the number of deaths.
     * <p>
     * This method finds the colour for the borough based on the number of deaths in the borough. The colour is
     * determined based on the following scheme:
     * 0 deaths: green
     * 1 death: yellow
     * 2 deaths: orange
     * 3 deaths: red
     * 4+ deaths: dark red
     * If the number of deaths is not in the range 0-3, the colour is set to dark red.
     *
     * @param n The number of deaths in the borough
     * @return The hex code for the colour of the borough
     */
    private String findColour(int n) {
        return switch (n) {
            case 0 -> "#007500"; //Hex code for green;
            case 1 -> "#FFCB2E"; //Hex code for yellow;
            case 2 -> "#FA6800"; //Hex code for orange;
            case 3 -> "#E32227"; //Hex code for red;
            default -> "#6D0E10"; //Hex code for dark red;
        };
    }

}
