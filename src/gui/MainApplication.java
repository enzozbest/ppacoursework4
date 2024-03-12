package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GUIController controller = new GUIController();
        controller.beginLoading();
        Scene scene = new Scene(controller.beginLoading());
        stage.setScene(scene);
        stage.show();
    }
}
