package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        GUIController controller = new GUIController();
        Scene scene = new Scene(controller.beginLoading());

        PlotTest plotTest = new PlotTest();
        plotTest.plotBarChart();
        Scene plotScene = plotTest.barChartScene();

        stage.setScene(plotScene);
        // stage.getIcons().add(new Image(<path to image>)) to change the icon of the window
        stage.show();
    }
}



