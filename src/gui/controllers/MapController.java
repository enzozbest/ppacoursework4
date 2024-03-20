package gui.controllers;

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
        this(null);
    }

    /**
     * Constructor for the MapController class
     *
     * @param queryString The query string to be used
     */
    private MapController(String queryString) {
        super(queryString);
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
                WelcomeController controller = new WelcomeController();
                controller.beginLoading();

                stage.setScene(controller.showWelcomeScreen());
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

    public Scene getMapFrame() {
        return new Scene(parent, 960, 600);
    }
}
