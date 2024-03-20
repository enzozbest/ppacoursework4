package gui;

import gui.controllers.WelcomeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the application.
 * <p>
 * This class is the entry point for the application. It creates the main stage and sets the scene to the welcome screen.
 * The welcome screen is then displayed to the user.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.20
 */
@SuppressWarnings("all")
public class EpiQuest extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        WelcomeController welcomeController = new WelcomeController();
        welcomeController.beginLoading();

        Scene welcomeScene = welcomeController.showWelcomeScreen();

        stage.setScene(welcomeScene);

       /*LocalDate startDate = welcomeController.getFromDate();
        LocalDate endDate = welcomeController.getToDate();

        StatisticsController statisticsController = new StatisticsController();
        statisticsController.initialiseDates(startDate, endDate);
        statisticsController.beginLoading();*/

        stage.setResizable(false);
        stage.setTitle("EpiQuest");
        stage.show();
    }

}



