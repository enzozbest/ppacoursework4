package gui.controllers;

import gui.components.AssetLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import utils.sql.queries.Query;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Class to control the map panel.
 * <p>
 * This class controls the map screen by setting the Map panel and displaying the map of London with its boroughs
 * delineated to the user. The boroughs are coloured based on the percentage of deaths in each borough for the selected
 * date range.
 * <p>
 * The user can hover over a borough to view the name of the borough, the total number of cases, and the number of
 * deaths. The borough is highlighted in black when hovered over, as well as being enlarged.
 * <p>
 * If a borough is clicked, the enlargement on hover effect for the other boroughs is disabled. The basic data for the clicked
 * borough remains displayed, but an additional "View Borough" button appears. The clicked borough will remain
 * highlighted in black and enlarged. If a different borough is clicked, the old borough will no longer be highlighted
 * and enlarged, and those effects will rather apply to the newly clicked borough. Hovering over the other boroughs will
 * cause the borders of that borough to be highlighted in black, but the borough will not be enlarged.
 * <p>
 * Clicking on the "View Borough" button displays all the data for the selected borough for the selected date range in a
 * new window, in a table format.
 * <p>
 * The user can navigate between this panel and the other panels in the application by using the navigation buttons
 * in the bottom left corner of the screen.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.27
 */
public class MapController extends AbstractController {

    @FXML
    private ListView list;
    @FXML
    private ImageView map_frame, mapImage, select_location;
    @FXML
    private Group map_group;
    @FXML
    private Text stack_borough_name, stack_button, stack_total_cases, stack_borough_deaths, back_text, forward_text;
    @FXML
    private VBox label_container;
    private AnchorPane parent;
    private LocalDate startDate, endDate;
    private HashMap<String, Integer[]> localBoroughRecords;

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
        super("SELECT borough, sum(new_deaths) AS borough_deaths, sum(new_cases) AS borough_cases " +
                "FROM covid_london " +
                "WHERE date BETWEEN '" + startDate + "'  AND '" + endDate + "' GROUP BY borough");

        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Method to begin loading the map screen.
     * <p>
     * This method loads the map screen by setting the map panel.
     * The map panel is then displayed to the user.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/fxml/map-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../resources/styles/default.css")).toExternalForm());
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../../resources/styles/map-view.css")).toExternalForm());
        } catch (IOException e) {
            System.out.println("Error loading the map frame!" + e.getMessage() + e.getCause() + Arrays.toString(e.getStackTrace()) + e.getLocalizedMessage());
        }

        this.setMapPanel();

        scene = new Scene(parent, 960, 600, Color.WHITE);
    }

    /**
     * Method to set the map panel.
     * <p>
     * This method sets the map panel by setting the background, map, back button, forward button, and borough colours.
     * The map panel is then displayed to the user.
     */
    private void setMapPanel() {
        this.setBackround();
        this.setSelectLocationText();
        this.setBackButton();
        this.setForwardButton();
        this.setMouseEvents(true);
        this.setStackPane();
        super.runBackgroundTask(super.queryDatabase(), () -> {
            this.processQuery();
            this.getPeriodDeaths();
            this.drawMap();
        });


    }

    /**
     * Method to set the background of the map.
     * <p>
     * This method sets the background of the map by setting the image of the map frame to the map frame image.
     * The image is then resized to fit the dimensions of the map frame, and the preserve ratio is set to true to
     * ensure the image is not distorted.
     */
    private void setBackround() {
        map_frame.setImage(AssetLoader.MAP_FRAME);
        map_frame.setFitWidth(960);
        map_frame.setFitHeight(600);
        map_frame.setPreserveRatio(true);
    }

    /**
     * Method to set the select location text.
     * <p>
     * This method sets the select location text by setting the image of the select location image view to the select
     * location image. The image is then resized to fit the dimensions of the image view, and the preserve ratio is set
     * to true to ensure the image is not distorted.
     */
    private void setSelectLocationText() {
        select_location.setImage(AssetLoader.SELECT_LOCATION);
        select_location.setFitWidth(180);
        select_location.setFitHeight(100);
        select_location.setPreserveRatio(true);
    }

    /**
     * Method to set the back button.
     * <p>
     * This method sets the back button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the back button. to ensure correct styling.
     */
    private void setBackButton() {
        back_text.getStyleClass().add("clickable");
        super.hoverFlash(back_text);
    }

    /**
     * Method to set the forward button.
     * <p>
     * This method sets the forward button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the forward button. to ensure correct styling.
     */
    private void setForwardButton() {
        forward_text.getStyleClass().add("clickable");
        super.hoverFlash(forward_text);
    }

    /**
     * Method to set the mouse events for the forward and back buttons.
     */
    private void setMouseEvents(boolean setEventsFlag) {
        if (setEventsFlag) {
            super.setNavigationEvents(true, back_text, forward_text, "welcome", "stats");
            return;
        }
        super.setNavigationEvents(false, back_text, forward_text, "welcome", "stats");
    }

    /**
     * Method to set the stack pane for the map.
     * <p>
     * This method sets the stack pane for the map by setting the font, width, wrap text, and scale of the labels
     * in the stack pane. The stack pane is then set to invisible.
     */
    private void setStackPane() {
        this.setViewBoroughButton();
        stack_borough_name.setVisible(false);
        stack_total_cases.setVisible(false);
        stack_borough_deaths.setVisible(false);
    }

    /**
     * Method to set the view borough button.
     * <p>
     * This method sets the view borough button by setting the text, visibility, style class, and indefinite flash for
     * the button.
     * The button is then set to invisible. It will not be displayed until a borough is clicked.
     */
    private void setViewBoroughButton() {
        stack_button.setText("View Borough");
        stack_button.setVisible(false);
        stack_button.getStyleClass().add("clickable");
        super.indefiniteFlash(stack_button);
    }

    /**
     * Method to draw the map.
     * <p>
     * This method draws the map by reading the coordinates of the boroughs from a file and creating SVG paths for each
     * borough. The SVG paths are then added to the map group.
     */
    private void drawMap() {
        File file = new File("/home/enzozbest/IdeaProjects/ppacoursework4/src/resources/map/london_boroughs_rough.coords");
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                String boroughCoordinates = reader.nextLine();
                String[] coordinates = boroughCoordinates.split(":");
                String svgPath = "M" + coordinates[1].replace(",", "") + " Z";
                String boroughName = coordinates[0];

                SVGPath boroughBoundary = new SVGPath();
                boroughBoundary.setContent(svgPath);
                boroughBoundary.setId("tile_" + String.join("", boroughName.split(" ")).toLowerCase());

                boroughBoundary.getStyleClass().clear();
                boroughBoundary.getStyleClass().add("clickable");
                boroughBoundary.getStyleClass().add("hoverable");

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
     */
    private void setBoroughClickEvent(SVGPath boroughBoundary, String boroughName) {
        boroughBoundary.setOnMouseClicked(event -> {
            this.disableHover();

            boroughBoundary.getStyleClass().add("clicked");
            boroughBoundary.getStyleClass().remove("clickable");
            boroughBoundary.getStyleClass().remove("hoverable");

            stack_button.setOnMouseClicked(mouseEvent -> createBoroughView(boroughName, startDate, endDate));
            stack_button.setVisible(true);

            stack_borough_name.setText(boroughName);
            stack_borough_name.setVisible(true);

            this.setStylesOtherBoroughs(boroughBoundary.getId());
        });
    }

    /**
     * Method to create the borough view.
     * <p>
     * This method creates the borough view by creating a new instance of the BoroughController class and calling the
     * beginLoading method. The borough view is then displayed to the user.
     * The borough view displays the data for the selected borough for the selected date range.
     */
    private void createBoroughView(String boroughName, LocalDate startDate, LocalDate endDate) {
        BoroughController controller = new BoroughController(boroughName, startDate, endDate);
        controller.beginLoading();
    }

    /**
     * Method to send every borough apart from the one whose id is passed as a parameter to the back.
     */
    private void setStylesOtherBoroughs(String id) {
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
            borough.getStyleClass().remove("clicked");
            borough.getStyleClass().remove("hoverable-scaling");
            borough.getStyleClass().add("clickable");
            borough.getStyleClass().add("hoverable");
        }
    }

    /**
     * Method to set the hover event for the boroughs on the map.
     */
    private void setBoroughHoverEvent(boolean setting, SVGPath boroughBoundary, String boroughName) {
        if (setting) {
            boroughBoundary.setOnMouseEntered(event -> {
                boroughBoundary.getStyleClass().add("hoverable-scaling");

                parent.lookup("#" + boroughBoundary.getId()).toFront();

                stack_borough_name.setText(boroughName);
                stack_borough_name.setVisible(true);

                stack_total_cases.setText("Total Cases: " + localBoroughRecords.get(boroughName)[0]);
                stack_total_cases.setVisible(true);

                stack_borough_deaths.setText("Deaths: " + localBoroughRecords.get(boroughName)[1]);
                stack_borough_deaths.setVisible(true);
            });
            boroughBoundary.setOnMouseExited(event -> {
                stack_borough_name.setVisible(false);
                stack_total_cases.setVisible(false);
                stack_borough_deaths.setVisible(false);

                parent.lookup("#" + boroughBoundary.getId()).toBack();
            });

            return;
        }

        boroughBoundary.getStyleClass().remove("hoverable-scaling");

        boroughBoundary.setOnMouseEntered(event -> {
        });
        boroughBoundary.setOnMouseExited(event -> {
        });
    }

    /**
     * Method to change the hover event for the boroughs on the map after the first click on a borough has happened.
     */
    private void disableHover() {
        for (Node path : map_group.getChildren()) {
            SVGPath svgPath = (SVGPath) path;
            this.setBoroughHoverEvent(false, svgPath, path.getId().substring(5));
        }
    }

    /**
     * Method to get the number of deaths in the selected date range.
     */
    private void getPeriodDeaths() {
        Query query = new Query("SELECT sum(new_deaths) as period_deaths FROM covid_london WHERE date BETWEEN '" + startDate + "' AND '" + endDate + "'");

        Task<ResultSet> task = super.queryDatabase(query);
        runBackgroundTask(task, () -> {
            try {
                ResultSet set = task.get();
                int periodDeaths = set.next() ? set.getInt("period_deaths") : 0;
                setBoroughColours(periodDeaths);
            } catch (SQLException | ExecutionException | InterruptedException e) {

            }
        });
    }

    /**
     * Method to set the colours of the boroughs on the map.
     * <p>
     * This method sets the colours of the boroughs on the map based on the number of deaths in each borough.
     * The colours are set based on the number of deaths in each borough, with the following scheme:
     * 0-1% deaths: green
     * 1-2% deaths: yellow
     * 2-3% deaths: orange
     * 3-4% deaths: red
     * 4%+ deaths: dark red
     */
    private void setBoroughColours(int periodDeaths) {
        for (String boroughName : localBoroughRecords.keySet()) {
            double death_percentage;
            if (periodDeaths == 0) {
                death_percentage = 0;
            } else {
                death_percentage = (double) localBoroughRecords.get(boroughName)[1] / periodDeaths;
            }

            String colour = findColour(death_percentage);
            SVGPath tile = (SVGPath) parent.lookup("#tile_" + String.join("", boroughName.split(" ")).toLowerCase());
            tile.setStyle("-fx-background-color: " + colour + "; -fx-fill: " + colour + ";");
        }
    }


    /**
     * Method to find the colour for the borough based on the number of deaths.
     * <p>
     * This method finds the colour for the borough based on the percentage of deaths in the borough. The colour is
     * determined based on the following scheme:
     * 0-1% deaths: green
     * 1-2% deaths: yellow
     * 2-3% deaths: orange
     * 3-4% deaths: red
     * 4%+ deaths: dark red
     */
    private String findColour(double death_percentage) {
        if (0 <= death_percentage && death_percentage < 0.01) {
            return "#007500";
        }
        if (0.01 < death_percentage && death_percentage < 0.02) {
            return "#FFCB2E";
        }
        if (0.02 < death_percentage && death_percentage < 0.03) {
            return "#FA6800";
        }
        if (0.03 < death_percentage && death_percentage < 0.04) {
            return "#E32227";
        }
        return "#6D0E10";
    }

    /**
     * Method to process the query for the map.
     * <p>
     * This method processes the query for the map by iterating through the data and storing the data in a map.
     * The map stores the borough name as the key and an array of the number of cases and deaths as the value.
     * The map is then stored in the localBoroughRecords field.
     */
    private void processQuery() {
        this.localBoroughRecords = new HashMap<>();
        try {
            while (data.next()) {
                Integer[] records = {data.getInt("borough_cases"), data.getInt("borough_deaths")};
                localBoroughRecords.put(data.getString("borough"), records);
            }
        } catch (SQLException e) {
            System.out.println("Error generating covid records");
        }
    }
}
