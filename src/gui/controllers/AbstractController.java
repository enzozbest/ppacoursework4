package gui.controllers;

import gui.SceneInitialiser;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.sql.queries.Query;
import utils.sql.queries.concurrent.QueryExecutor;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class to be extended by all Controllers in the application.
 * <p>
 * This class provides the basic functionality for all controllers in the application. It provides the ability to query
 * the database, set mouse events for the forward and back buttons, and add click events to image views. It also provides
 * the ability to flash an image view when the mouse hovers over it.
 * <p>
 * This class is abstract and should not be instantiated directly. It is designed to be extended by other classes.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
 */
public abstract class AbstractController implements Controller {

    protected ResultSet data;
    protected Scene scene;
    private Query query;

    /**
     * No-argument Constructor for the AbstractController class.
     */
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
        query = new Query(queryString);
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
    protected void setNavigationEvents(boolean setting, Node back, Node forward, String backScene, String forwardScene) {
        if (setting) {
            this.addClickEvent(back, backScene);
            this.addClickEvent(forward, forwardScene);
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
     * @param node      The image view to add the click event to
     * @param nextScene The next scene to change to
     */
    private void addClickEvent(Node node, String nextScene) {
        node.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = SceneInitialiser.scenes.get(nextScene);
            stage.setScene(scene);
            scene.setCursor(Cursor.DEFAULT);
            scene.getRoot().setCursor(Cursor.DEFAULT);
        });
    }

    /**
     * Method to flash a node when the mouse hovers over it.
     * <p>
     * This method creates a fade transition to flash the node when the mouse hovers over it. The transition is played
     * when the mouse enters the node. The transition is stopped when the mouse exits the node.
     *
     * @param node The node to flash
     */
    protected void hoverFlash(Node node) {
        FadeTransition transition = createFadeTransition(node);
        transition.setCycleCount(2);

        node.setOnMouseEntered(event -> {
            transition.setOnFinished(event1 -> transition.play());
            transition.play();
        });

        node.setOnMouseExited(event -> transition.setOnFinished(event2 -> transition.stop()));
    }

    /**
     * Method to indefinitely flash a node.
     * <p>
     * This method creates a fade transition to flash the node indefinitely. The transition is played indefinitely.
     *
     * @param node The node to flash
     */
    protected void indefiniteFlash(Node node) {
        FadeTransition transition = createFadeTransition(node);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
    }

    /**
     * Method to create a fade transition.
     * <p>
     * This method creates a fade transition for the specified node. The transition is set to fade from 1.0 to 0.3 and
     * back to 1.0. The duration of the transition is set to 1150 milliseconds.
     *
     * @param node The node to create the fade transition for
     * @return The fade transition
     */
    private FadeTransition createFadeTransition(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(1150), node);
        transition.setFromValue(1.0);
        transition.setToValue(0.3);
        transition.setAutoReverse(true);
        return transition;
    }

    protected void runBackgroundTask(Task<ResultSet> task, Runnable onSucceeded) {

        task.setOnSucceeded(event -> {
            try {
                data = task.get();
            } catch (ExecutionException | InterruptedException e) {
                System.out.println("Error processing background thread" + e.getMessage() + e.getCause());
            }
            onSucceeded.run();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Method to query the database.
     * <p>
     * This method queries the database using the provided query object. It then returns the result set from the query.
     *
     * @return The result set from the query
     */
    protected Task<ResultSet> queryDatabase() {
        Task<ResultSet> task = new Task<>() {
            @Override
            protected ResultSet call() {
                try {
                    ResultSet ret = new QueryExecutor(query).runQuery().get();
                    return ret;
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error processing background thread" + e.getMessage() + e.getCause());
                }
                return null;
            }
        };
        return task;
    }

    /**
     * Method to query the database.
     * <p>
     * This method queries the database using the provided query object. It then returns the result set from the query.
     *
     * @return The result set from the query
     */
    protected Task<ResultSet> queryDatabase(Query query) {
        Task<ResultSet> task = new Task<>() {
            @Override
            protected ResultSet call() {
                try {
                    ResultSet ret = new QueryExecutor(query).runQuery().get();
                    return ret;
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error processing background thread" + e.getMessage() + e.getCause());
                }
                return null;
            }
        };
        return task;
    }

    /**
     * @return The scene represented by the Controller
     */
    public Scene getScene() {
        return scene;
    }
}
