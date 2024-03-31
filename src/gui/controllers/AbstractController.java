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
 * the database, set mouse events for the forward and back buttons, and add click events to nodes. It also provides the
 * ability to flash a node when the mouse hovers over it, or indefinitely.
 * <p>
 * This class is abstract and should not be instantiated directly. It is designed to be extended by other classes.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.28
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
     * @param addEventFlag true to add mouse events, false to remove events
     * @param back         the back button
     * @param forward      the forward button
     * @param backScene    the scene to change to when the back button is clicked
     * @param forwardScene the scene to change to when the forward button is clicked
     */
    protected void setNavigationEvents(boolean addEventFlag, Node back, Node forward, String backScene, String forwardScene) {
        if (addEventFlag) {
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
     * Method to add click event handler to a node.
     * <p>
     * This method adds the click event handler to change the scene to the next scene.
     */
    private void addClickEvent(Node node, String nextScene) {
        node.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = SceneInitialiser.SCENES.get(nextScene);
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
     * This method creates a fade transition to flash the node indefinitely.
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
     */
    private FadeTransition createFadeTransition(Node node) {
        FadeTransition transition = new FadeTransition(Duration.millis(1150), node);
        transition.setFromValue(1.0);
        transition.setToValue(0.3);
        transition.setAutoReverse(true);
        return transition;
    }

    /**
     * Method to run a background task.
     * <p>
     * This method runs a background task using the provided task object. It then sets the data from the task to the
     * data field of the controller. Finally, it runs the onSucceeded runnable.
     *
     * @param task        the task to run
     * @param onSucceeded the runnable to run when the task is succeeded
     */
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
     * Method to create a task.
     * <p>
     * This method creates a task using the provided query object. The task runs the query in the background and returns
     * the result set from the query.
     */
    private Task<ResultSet> createTask(Query query) {
        return new Task<>() {
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
    }

    /**
     * Method to query the database.
     * <p>
     * This method queries the database using the query object passed at construction.
     *
     * @return A task that will return the result set from the query.
     */
    protected Task<ResultSet> queryDatabase() {
        return createTask(query);
    }

    /**
     * Method to query the database.
     * <p>
     * This method queries the database using the provide query object.
     *
     * @return A task that will return the result set from the query.
     */
    protected Task<ResultSet> queryDatabase(Query query) {
        return createTask(query);
    }

    /**
     * Method to get the scene controlled by the Controller.
     *
     * @return The scene controlled by the Controller
     */
    public Scene getScene() {
        return scene;
    }
}
