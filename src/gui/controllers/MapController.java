package gui.controllers;

import gui.SceneInitialiser;
import gui.components.ImageLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MapController extends AbstractController {

    private AnchorPane parent;

    @FXML
    private ListView list;

    @FXML
    private ImageView backText;

    @FXML
    private WebView webViewMap;

    /**
     * No-argument constructor for the MapController class
     */
    public MapController() {
        this(null, null);
    }

    /**
     * Constructor for the MapController class
     *
     * @param startDate The start date of the date range
     * @param endDate   The end date of the date range
     */
    public MapController(LocalDate startDate, LocalDate endDate) {
        super("SELECT borough, sum(total_cases) AS total FROM covid_london WHERE date BETWEEN '" + startDate + "' AND '" + endDate + "' GROUP BY borough;");
    }

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

        setBackText();
    }

    /**
     * Method to set the back button.
     * <p>
     * This method sets the back button by setting the image, size, and mouse events for it. It also adds the
     * style class "clickable" to the back button. to ensure correct styling.
     */
    private void setBackText() {
        backText.setImage(ImageLoader.BACK);
        backText.setFitWidth(110);
        backText.setFitHeight(40);
        backText.setPreserveRatio(true);
        setMouseEvents(true);
        backText.getStyleClass().add("clickable");
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
            backText.setOnMouseEntered(mouseEvent -> backText.setImage(ImageLoader.BACK));
            backText.setOnMouseExited(mouseEvent -> backText.setImage(ImageLoader.BACK));
            backText.setOnMouseClicked(mouseEvent -> {


                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

                stage.setScene(SceneInitialiser.scenes.get("welcome"));
            });
            return;
        }
        backText.setOnMouseEntered(mouseEvent -> {
        });
        backText.setOnMouseExited(mouseEvent -> {
        });
        backText.setOnMouseClicked(mouseEvent -> {
        });
    }

    /**
     * @return The scene for the map screen
     */
    public Scene getMapScene() {
        return new Scene(parent, 960, 600);
    }
}
