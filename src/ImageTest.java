import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ImageTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane pane = new AnchorPane();
        Image image = new Image("backdrop.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setVisible(true);
        pane.getChildren().add(imageView);

        stage.setScene(new Scene(pane, 960, 600));
        stage.show();
    }
}
