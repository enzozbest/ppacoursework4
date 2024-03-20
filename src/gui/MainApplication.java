package gui;

import gui.controllers.WelcomeController;
import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WelcomeController controller = new WelcomeController();
        controller.beginLoading();
        controller.showWelcomeScreen();

/*
        MapController controller = new MapController();
        controller.beginLoading();
        controller.showMapFrame();*/
    }
}



