package gui.controllers;

import gui.SceneInitialiser;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.sql.queries.Query;
import utils.sql.queries.QueryProcessor;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class to be extended by all controllers in the application.
 * <p>
 * This class provides a common interface for all controllers to query the database and process the results.
 * It also provides a common interface for all controllers to handle the results of the query.
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
 */
public abstract class AbstractController implements Controller {

    protected Query query;
    protected QueryExecutor executor;
    protected QueryProcessor processor;

    protected ResultSet data;

    protected Scene scene;

    protected AbstractController() {
    }

    /**
     * Constructor for the AbstractController class.
     *
     * @param queryString the query string to be executed.
     */
    protected AbstractController(String queryString) {

        if (queryString == null) {
            return;
        }

        Query query = new Query(queryString);
        data = queryDatabase(query);
    }

    /**
     * Method to query the database.
     */
    private ResultSet queryDatabase(Query query) {

        ResultSet ret;
        try {
            ret = new QueryExecutor(query).runQuery().get();
            return ret;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error generating result set! \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }
        return null;
    }


    /**
     * Method to set the mouse events for the forward and back buttons.
     * <p>
     * This method sets the mouse events for the forward and back buttons by adding or removing the specified events
     * from the buttons. This is used to change the behaviour of the buttons when the user is navigating between
     * different scenes.
     *
     * @param setting Whether to add or remove the mouse events
     */
    protected void setNavigationEvents(boolean setting, ImageView back, ImageView forward, String backScene, String forwardScene) {
        if (setting) {
            addClickEvent(back, backScene);
            addClickEvent(forward, forwardScene);
            return;
        }

        back.setOnMouseClicked(mouseEvent -> {
        });
        forward.setOnMouseClicked(mouseEvent -> {
        });
    }

    /**
     * Method to add the click event to the image view.
     * <p>
     * This method adds the click event to the image view by setting the on mouse clicked event to change the scene
     * to the next scene.
     *
     * @param imageView The image view to add the click event to
     * @param nextScene The next scene to change to
     */
    private void addClickEvent(ImageView imageView, String nextScene) {
        imageView.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = SceneInitialiser.scenes.get(nextScene);
            stage.setScene(scene);
            scene.setCursor(Cursor.DEFAULT);
            scene.getRoot().setCursor(Cursor.DEFAULT);
        });
    }

    public Scene getScene() {
        return scene;
    }
}
