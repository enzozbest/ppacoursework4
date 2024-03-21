package gui.controllers;

import gui.SceneInitialiser;
import gui.components.ImageLoader;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller for the welcome screen.
 * <p>
 * This controller is responsible for the welcome screen, which is the first screen the user sees when they open the application.
 * The welcome screen allows the user to select a date range to view data for, and then proceed to the main application.
 * The user can select a date range by selecting a start date and an end date from the drop-down menus.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.19
 */
@SuppressWarnings({"unused", "unchecked"})
public class WelcomeController extends AbstractController {

    private final BooleanProperty isValidDateRange;
    private AnchorPane parent;
    @FXML
    private ImageView welcomeBackdrop, guiTitle, guiSubtitle, guiCharacter;
    @FXML
    private StackPane stackPane;
    @FXML
    private ComboBox from, to;

    /**
     * Constructor for the WelcomeController.
     */
    public WelcomeController() {
        super("SELECT DISTINCT `date` FROM covid_london ORDER BY `date` ASC");
        isValidDateRange = new SimpleBooleanProperty();
    }


    /**
     * Method to begin loading the welcome screen.
     * <p>
     * This method loads the welcome screen from the welcome-screen.fxml file and sets the controller to this class.
     * It then sets the available dates in the ListViews for the user to select from.
     * The backdrop is added to the screen via the setWelcomeBackdrop() method, and the title and character are added
     * via calls to setGuiTitle() and setGuiCharacter() methods respectively.
     * <p>
     * Listeners are added to the ListViews so that validation can be performed on the selected dates.
     * <p>
     * Finally, it displays the welcome screen.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/welcome-screen.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
            parent.getStylesheets().add(getClass().getResource("../styles/default.css").toExternalForm());
        } catch (IOException e) {
            System.out.println("Error loading welcome-screen.fxml \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }

        isValidDateRange.set(false);

        addListeners();

        setAvailableDates();

        setWelcomeBackdrop();

        setGuiTitle();

        setGuiSubtitle();

        setGuiCharacter();
    }

    /**
     * Method to add listeners to components of the GUI.
     * <p>
     * This method adds listeners to eac <em>ComboBox</em> to watch for when the user selects the start and end dates from.
     * These listeners call the validateDates() method to ensure that the selected date range is valid.
     * <p>
     * The isValidDateRange listener is added to the isValidDateRange property to change the subtitle image when the
     * date range is valid or invalid.
     */
    private void addListeners() {
        from.valueProperty().addListener((observableValue, oldValue, newValue) -> setInteractiveElements());
        to.valueProperty().addListener((observableValue, oldValue, newValue) -> setInteractiveElements());

        isValidDateRange.addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                guiSubtitle.setImage(ImageLoader.PRESS_TO_START);
            } else {
                guiSubtitle.setImage(ImageLoader.ENTER_DATE_RANGE);
            }
        });
    }

    /**
     * Method to validate the selected dates.
     * <p>
     * This method validates the selected dates by checking if the start date is before the end date.
     * If the start date is after the end date, an error message is displayed to the user on the screen.
     * When the user corrects the dates, the error message is hidden.
     * <p>
     * Upon validation, the isValidDateRange property is set to true or false, and the style class and mouse events
     * for the subtitle are set accordingly.
     */
    private void setInteractiveElements() {
        if (from.getValue() == null || to.getValue() == null) {
            return;
        }
        LocalDate fromDate = formatDate(from.getValue().toString());
        LocalDate toDate = formatDate(to.getValue().toString());

        if (!(fromDate.isBefore(toDate) || fromDate.equals(toDate))) {
            isValidDateRange.set(false);
            setStyleClass("clickable", false);
            setMouseEvents(false);
            return;
        }
        isValidDateRange.set(true);
        setStyleClass("clickable", true);
        setMouseEvents(true);
    }

    /**
     * Method to set the mouse events for the subtitle.
     * <p>
     * This method sets the mouse events for the subtitle by adding or removing the specified events from the subtitle.
     * This is used to change the behaviour of the subtitle when the date range is valid or invalid.
     *
     * @param setting Whether to add or remove the mouse events
     */
    private void setMouseEvents(boolean setting) {
        if (setting) {
            guiSubtitle.setOnMouseEntered(mouseEvent -> guiSubtitle.setImage(ImageLoader.PRESS_TO_START));
            guiSubtitle.setOnMouseExited(mouseEvent -> guiSubtitle.setImage(ImageLoader.PRESS_TO_START));
            guiSubtitle.setOnMouseClicked(mouseEvent -> {


                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

                stage.setScene(SceneInitialiser.scenes.get("map"));
            });
            return;
        }
        guiSubtitle.setOnMouseEntered(mouseEvent -> {
        });
        guiSubtitle.setOnMouseExited(mouseEvent -> {
        });
        guiSubtitle.setOnMouseClicked(mouseEvent -> {
        });
    }

    /**
     * Method to set the style class for the subtitle.
     * <p>
     * This method sets the style class for the subtitle by adding or removing the specified class from the subtitle.
     * This is used to change the appearance of the subtitle when the date range is valid or invalid.
     *
     * @param className The name of the style class to add or remove
     * @param setting   Whether to add or remove the style class
     */
    private void setStyleClass(String className, boolean setting) {
        if (setting) {
            guiSubtitle.getStyleClass().add(className);
            return;
        }
        guiSubtitle.getStyleClass().remove(className);
    }


    /**
     * Method to format the date.
     * <p>
     * This method formats the date by splitting the date string into its parts and creating a LocalDate object from it.
     * This ensures that the date is in an appropriate format for display, as well as guarantees that the date does not
     * contain any information about the time.
     *
     * @param sqlDate The date string to format, usually retrieved .from the database
     * @return The formatted date as a LocalDate object
     */
    private LocalDate formatDate(String sqlDate) {
        String[] dateParts = sqlDate.split("-");
        return LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
    }

    /**
     * Method to set the available dates in the ListViews.
     * <p>
     * This method sets the available dates in the ListViews by iterating over the ResultSet from the database query.
     * It adds each date to the ListViews for the user to select from, in an appropriate format, namely 'YYYY-MM-DD'.
     */
    private void setAvailableDates() {
        try {
            while (data.next()) {
                String date = data.getString("date").split(" ")[0];

                from.getItems().add(date);
                to.getItems().add(date);
            }
        } catch (SQLException e) {
            System.out.println("Error getting available dates \n" + e.getMessage() + "\n" + e.getCause() + "\n" +
                    e.getStackTrace());
        }
    }

    /**
     * Method to set the backdrop for the welcome screen.
     * <p>
     * This method sets the backdrop for the welcome screen by loading the backdrop image from the resources folder.
     * The backdrop is then added to the screen as an ImageView.
     */
    private void setWelcomeBackdrop() {

        welcomeBackdrop.setImage(ImageLoader.BACKDROP);

        welcomeBackdrop.setFitWidth(960);
        welcomeBackdrop.setFitHeight(600);
        welcomeBackdrop.setPreserveRatio(false);
        welcomeBackdrop.setVisible(true);
    }

    /**
     * Method to set the title for the welcome screen.
     * <p>
     * This method sets the title for the welcome screen by loading the title image from the resources folder.
     * The title is then added to the screen as an ImageView.
     */
    private void setGuiTitle() {
        guiTitle.setImage(ImageLoader.TITLE); //doesnt exist yet!

        guiTitle.setFitWidth(477);
        guiTitle.setFitHeight(114);
        guiTitle.setPreserveRatio(false);

    }

    /**
     * Method to set the subtitle for the welcome screen.
     * <p>
     * This method sets the subtitle for the welcome screen by loading the subtitle image from the resources folder.
     * The subtitle is then added to the screen as an ImageView.
     * A new thread is created to animate the subtitle by changing the brightness of the image without blocking the main
     * GUI. This animation translates to a flashing text effect, characteristic of old-style video games.
     */
    private void setGuiSubtitle() {
        guiSubtitle.setImage(ImageLoader.ENTER_DATE_RANGE);

        guiSubtitle.setFitWidth(484);
        guiSubtitle.setFitHeight(66);

        new Thread(() -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            float delta = -1f / 24;
            float brightness = 0.5f;

            colorAdjust.setBrightness(brightness);
            guiSubtitle.setEffect(colorAdjust);
            while (true) {
                for (int i = 0; i < 24; i++) {
                    brightness += delta;
                    colorAdjust.setBrightness(brightness);
                    Platform.runLater(() -> guiSubtitle.setEffect(colorAdjust));
                    try {
                        Thread.sleep(42);
                    } catch (InterruptedException e) {
                    }
                }
                delta *= -1;
            }
        }).start();
    }

    /**
     * Method to set the character for the welcome screen.
     * <p>
     * This method sets the character for the welcome screen by loading the character image from the resources folder.
     * The character is then added to the screen as an ImageView.
     */
    private void setGuiCharacter() {
        guiCharacter.setImage(ImageLoader.CHARACTER);

        guiCharacter.setFitWidth(340);
        guiCharacter.setFitHeight(424);
        guiCharacter.setPreserveRatio(false);
    }

    /**
     * Method to show the welcome screen.
     * <p>
     * This method shows the welcome screen to the user by creating a new Stage and setting the scene to the welcome screen.
     * The stage is then displayed to the user.
     */
    public Scene getWelcomeScene() {
        return new Scene(parent, 960, 600);
    }

    /**
     * @return The start date selected by the user.
     */
    public LocalDate getFromDate() {
        if (from.getValue() == null) {
            return null;
        }
        return formatDate(from.getValue().toString());
    }

    /**
     * @return The end date selected by the user.
     */
    public LocalDate getToDate() {
        if (to.getValue() == null) {
            return null;
        }
        return formatDate(to.getValue().toString());
    }
}
