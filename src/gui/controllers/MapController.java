package gui.controllers;

import gui.components.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("rawtypes")
public class MapController extends AbstractController {

    @FXML
    private ListView list;
    @FXML
    private ImageView backText, mapFrame, mapImage, forwardText;
    @FXML
    private WebView webViewMap;
    @FXML
    private Group map_group;
    @FXML
    private Label stack_borough_name, stack_borough_deaths, stack_total_cases, stack_title;
    @FXML
    private VBox label_container;
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
        scene = new Scene(parent, 960, 600, Color.WHITE);
    }

    /**
     * Method to set the map panel.
     * <p>
     * This method sets the map panel by setting the background, map, back button, forward button, and borough colours.
     * The map panel is then displayed to the user.
     */
    private void setMapPanel() {
        setBackround();
        setStackPane();
        setBackButton();
        setForwardButton();
        drawMap();
        //setBoroughColours();
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
     * Method to set the stack pane for the map.
     * <p>
     * This method sets the stack pane for the map by setting the font, width, wrap text, and scale of the labels
     * in the stack pane. The stack pane is then set to invisible.
     */
    private void setStackPane() {
        stack_title.setFont(AssetLoader.EQ_FONT);
        stack_title.prefWidthProperty().bind(label_container.widthProperty());
        stack_title.setWrapText(true);
        stack_title.setScaleX(0.5);
        stack_title.setScaleY(0.5);

        stack_borough_name.setFont(AssetLoader.EQ_FONT);
        stack_borough_name.prefWidthProperty().bind(label_container.widthProperty());
        stack_borough_name.setWrapText(true);
        stack_borough_name.setScaleX(0.5);
        stack_borough_name.setScaleY(0.5);

        stack_total_cases.setFont(AssetLoader.EQ_FONT);
        stack_total_cases.prefWidthProperty().bind(label_container.widthProperty());
        stack_total_cases.setWrapText(true);
        stack_total_cases.setScaleX(0.5);
        stack_total_cases.setScaleY(0.5);

        stack_borough_deaths.setFont(AssetLoader.EQ_FONT);
        stack_borough_deaths.prefWidthProperty().bind(label_container.widthProperty());
        stack_borough_deaths.setWrapText(true);
        stack_borough_deaths.setScaleX(0.5);
        stack_borough_deaths.setScaleY(0.5);

        stack_title.setVisible(false);
        stack_borough_name.setVisible(false);
        stack_total_cases.setVisible(false);
        stack_borough_deaths.setVisible(false);
    }

    /**
     * Method to draw the map.
     * <p>
     * This method draws the map by reading the coordinates of the boroughs from a file and creating SVG paths for each
     * borough. The SVG paths are then added to the map group.
     */
    private void drawMap() {

        File file = new File("/home/enzozbest/IdeaProjects/ppacoursework4/src/gui/map/london_boroughs.coords");
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                /*
                StackPane tile = new StackPane();
                String boroughCoordinates = reader.nextLine();
                String[] coordinates = boroughCoordinates.split(":");
                String boroughName = coordinates[0];
                String svgPath = "M " + coordinates[1].replace(",", "") + " Z";
                String hexCode = String.format("#%06X", (int) (Math.random() * 0x1000000));

                tile.setId("tile_" + String.join("", boroughName.split(" ")).toLowerCase());
                tile.setStyle("-fx-background-color: " + hexCode + ";" +
                        "-fx-shape: \"" + svgPath + "\";" +
                        "-fx-border-color: black;" +
                        "-fx-fill: " + hexCode + ";");
                map_group.getChildren().add(tile);*/

                String boroughCoordinates = reader.nextLine();
                String[] coordinates = boroughCoordinates.split(":");
                String svgPath = "M " + coordinates[1].replace(",", "") + " Z";
                String boroughName = coordinates[0];
                SVGPath boroughBoundary = new SVGPath();
                boroughBoundary.setContent(svgPath);
                boroughBoundary.setId("tile_" + String.join("", boroughName.split(" ")).toLowerCase());
                String hexCode = String.format("#%06X", (int) (Math.random() * 0x1000000));
                boroughBoundary.setStyle("-fx-fill: " + hexCode + ";" + "-fx-border-style: solid;" + "-fx-border-width: thin;");
                boroughBoundary.getStyleClass().add("clickable");
                setBoroughClickEvent(boroughBoundary, boroughName);
                setBoroughHoverEvent(true, boroughBoundary, boroughName);
                map_group.getChildren().add(boroughBoundary);
            }
        } catch (IOException e) {
            System.out.println("Error reading the coordinates file! " + e.getMessage() + e.getCause() + e.getStackTrace());
        }

    }

    /**
     * Method to set the click event for the boroughs on the map.
     *
     * @param boroughBoundary The SVG path for the borough
     * @param boroughName     The name of the borough
     */
    private void setBoroughClickEvent(SVGPath boroughBoundary, String boroughName) {
        boroughBoundary.setOnMouseClicked(event -> {
            boroughBoundary.setScaleX(1.3);
            boroughBoundary.setScaleY(1.3);
            disableHover();
            boroughBoundary.setStroke(Paint.valueOf("black"));
            boroughBoundary.setOnMouseExited(event1 -> {
                boroughBoundary.setStroke(Paint.valueOf("black"));
            });
            parent.lookup("#" + boroughBoundary.getId()).toFront();
            stack_title.setVisible(true);
            stack_borough_name.setText(boroughName);
            stack_borough_name.setVisible(true);
            sendEverythingToBack(boroughBoundary.getId());
        });
    }

    /**
     * Method to send every borough apart from the one whose id is passed as a parameter to the back.
     *
     * @param id The id of the borough to send to the front
     */
    private void sendEverythingToBack(String id) {
        ArrayList<String> ids = new ArrayList<>();
        for (Node path : map_group.getChildren()) {
            if (path.getId().equals(id)) {
                continue;
            }
            ids.add(path.getId());
        }
        for (String boroughID : ids) {
            SVGPath borough = (SVGPath) parent.lookup("#" + boroughID);

            borough.setScaleX(1);
            borough.setScaleY(1);
            borough.setStroke(Paint.valueOf("transparent"));
            borough.toBack();
        }
    }

    /**
     * Method to set the hover event for the boroughs on the map.
     *
     * @param setting         Whether to add or remove the hover event
     * @param boroughBoundary The SVG path for the borough
     * @param boroughName     The name of the borough
     */
    private void setBoroughHoverEvent(boolean setting, SVGPath boroughBoundary, String boroughName) {
        if (setting) {
            boroughBoundary.setOnMouseEntered(event -> {
                boroughBoundary.setScaleX(1.3);
                boroughBoundary.setScaleY(1.3);
                boroughBoundary.setStroke(Paint.valueOf("black"));
                parent.lookup("#" + boroughBoundary.getId()).toFront();
                stack_title.setVisible(true);
                stack_borough_name.setText(boroughName);
                stack_borough_name.setVisible(true);
            });

            boroughBoundary.setOnMouseExited(event -> {
                boroughBoundary.setScaleX(1);
                boroughBoundary.setScaleY(1);
                boroughBoundary.setStroke(Paint.valueOf("transparent"));
                stack_title.setVisible(false);
                stack_borough_name.setVisible(false);
                parent.lookup("#" + boroughBoundary.getId()).toBack();
            });

            return;
        }

        boroughBoundary.setOnMouseEntered(event -> {
            boroughBoundary.setStroke(Paint.valueOf("black"));
        });

        boroughBoundary.setOnMouseExited(event -> {
            boroughBoundary.setStroke(Paint.valueOf("transparent"));
        });

    }

    /**
     * Method to change the hover event for the boroughs on the map after the first click on a borough has happened.
     */
    private void disableHover() {
        for (Node path : map_group.getChildren()) {
            SVGPath svgPath = (SVGPath) path;
            setBoroughHoverEvent(false, svgPath, path.getId().substring(5));
        }
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
                String boroughName = String.join("", data.getString("borough").split(" ")).toLowerCase();
                String colour = findColour(deaths);
                SVGPath tile = (SVGPath) parent.lookup("#tile_" + boroughName);
                tile.setStyle("-fx-background-color: " + colour + ";");
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
