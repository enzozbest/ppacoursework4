package gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for the application.
 * <p>
 * This class is the entry point for the application. It creates the main stage and sets the scene to the welcome screen.
 * The welcome screen is then displayed to the user.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.27
 */
public class EpiQuest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method for the application.
     */
    @Override
    public void start(Stage stage) {
        SceneInitialiser.getInstance();

        stage.setScene(SceneInitialiser.scenes.get("welcome"));
        stage.setResizable(false);
        stage.setTitle("EpiQuest");
        stage.show();
    }
}



