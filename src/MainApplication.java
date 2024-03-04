import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MainApplication extends Application {

    @FXML
    private Label testLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        fxmlLoader.setController(this);

        CovidDataLoader loader = new CovidDataLoader();
        ArrayList<CovidRecord> records = loader.load();

        try {
            BorderPane parentPane = fxmlLoader.load();
            testLabel.setText(records.get(0).toString());
            Scene scene = new Scene(parentPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage() + e.getStackTrace() + e.getCause());
        }
    }
}
