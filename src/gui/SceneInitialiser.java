package gui;

import gui.controllers.GraphController;
import gui.controllers.MapController;
import gui.controllers.StatisticsController;
import gui.controllers.WelcomeController;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create the scenes for the application.
 * <p>
 * This class creates the scenes for the application and stores them in a map.
 * The scenes are created in separate threads to allow the user to select the dates before the scenes are created.
 * The scenes are then stored in a map for easy access throughout the program.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
 */
public class SceneInitialiser {

    /**
     * Map to store the scenes for the application. The key is a string representing the scene, and the value is the
     * Scene object.
     */
    public static Map<String, Scene> scenes = new HashMap<>();
    private static SceneInitialiser instance;
    private final WelcomeController welcomeController;
    private LocalDate startDate, endDate;

    /**
     * Constructor for the Scenes class.
     * <p>
     * Creates the scenes for the application.
     */
    public SceneInitialiser() {
        welcomeController = new WelcomeController();
        createScenes();
    }

    /**
     * @return the instance of the SceneInitialiser class.
     */
    public static SceneInitialiser getInstance() {
        if (instance == null) {
            instance = new SceneInitialiser();
        }
        return instance;
    }

    /**
     * Method to create the scenes for the application.
     * <p>
     * Creates the welcome scene, map scene, and statistics scene.
     * The scenes are created in separate threads to allow the user to select the dates before the scenes are created.
     * The scenes are then stored in a map for easy access throughout the program.
     */
    private void createScenes() {
        //Create the welcome scene
        welcomeController.beginLoading();
        Scene welcomeScene = welcomeController.getScene();
        scenes.put("welcome", welcomeScene);

        //Use a separate thread to wait for the user to select the dates before setting the fields and creating the next
        // scenes.
        waitForDates();
    }

    /**
     * Method to wait for the user to select the start and end dates.
     */
    public void waitForDates() {
        new Thread(() -> {
            while (welcomeController.getFromDate() == null || welcomeController.getToDate() == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Error waiting for dates" + e.getMessage() + e.getCause() + Arrays.toString(e.getStackTrace()));
                }
            }

            startDate = welcomeController.getFromDate();
            endDate = welcomeController.getToDate();

            //Create other scenes once dates have been chosen by the user on the JavaFX thread.
            Platform.runLater(() -> {
                createMapScene();
                createStatisticsScene();
                createGraphScene();
            });
        }).start();
    }

    /**
     * Method to update the scenes when the user changes the selected date range.
     * <p>
     * Remove the map and statistics scenes from the scenes map and wait until dates are set to create the other
     * application scenes.
     */
    public void updateScenes() {
        scenes.remove("map");
        scenes.remove("stats");
        scenes.remove("graph");
        waitForDates();
    }

    /**
     * Method to create the map scene.
     * <p>
     * Creates the map scene and stores it in the scenes map.
     * The map scene is created in the main JavaFX thread, but this method should only be called once the start and end
     * dates have been selected. In our implementation this is achieved by calling Platform.runLater() in the thread
     * that checks for the selected dates.
     */
    private void createMapScene() {
        MapController mapController = new MapController(startDate, endDate);
        mapController.beginLoading();
        Scene mapScene = mapController.getScene();

        scenes.put("map", mapScene);
    }

    /**
     * Method to create the statistics scene.
     * <p>
     * Creates the statistics scene and stores it in the scenes map.
     * The statistics scene is created in the main JavaFX thread, but this method should only be called once the start and
     * end dates have been selected. In our implementation this is achieved by calling Platform.runLater() in the thread
     * that checks for the selected dates.
     */
    private void createStatisticsScene() {
        StatisticsController statisticsController = new StatisticsController(startDate, endDate);
        statisticsController.beginLoading();
        Scene statisticsScene = statisticsController.getScene();

        scenes.put("stats", statisticsScene);
    }

    /**
     * Method to create the graph scene.
     * <p>
     * Creates the graph scene and stores it in the scenes map.
     * The graph scene is created in the main JavaFX thread, but this method should only be called once the start and
     * end dates have been selected. In our implementation this is achieved by calling Platform.runLater() in the thread
     * that checks for the selected dates.
     */
    private void createGraphScene() {
        GraphController graphController = new GraphController(startDate, endDate);
        graphController.beginLoading();
        Scene graphScene = graphController.getScene();

        scenes.put("graph", graphScene);
    }
}
