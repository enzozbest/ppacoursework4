package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MapController extends AbstractController {


    private AnchorPane parent;

    @FXML
    private ListView list;

    @FXML
    private WebView webViewMap;

    /**
     * Constructor for the MapController class
     */
    public MapController() {
        super(null);
    }

    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/map-frame.fxml"));

        try {
            parent = loader.load();

        } catch (IOException e) {
            System.out.println("Error loading the map frame!" + e.getMessage() + e.getCause() + e.getStackTrace() + e.getLocalizedMessage());
        }
    }

    public void showMapFrame() {
        Stage stage = new Stage();
        stage.setTitle("Map View");
        stage.setResizable(false);

        Scene scene = new Scene(parent, 960, 600);
        stage.setScene(scene);
        stage.show();

    }
}
