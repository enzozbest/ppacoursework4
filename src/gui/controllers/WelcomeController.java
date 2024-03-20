package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    private AnchorPane parent;

    @FXML
    private ImageView welcomeBackdrop, guiTitle, guiSubtitle, guiCharacter;
    @FXML
    private Label errorMessage;
    @FXML
    private StackPane stackPane;
    @FXML
    private ComboBox from, to;

    /**
     * Constructor for the WelcomeController.
     */
    public WelcomeController() {
        super("SELECT DISTINCT `date` FROM covid_london ORDER BY `date` ASC");
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
        } catch (IOException e) {
            System.out.println("Error loading welcome-screen.fxml \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }

        addListeners();

        setAvailableDates();
        setWelcomeBackdrop();

        /* Uncomment when assets available
        setWelcomeBackdrop();
        setGuiTitle();
        setGuiCharacter();*/

    }

    /**
     * Method to add listeners to the ListViews.
     * <p>
     * This method adds listeners to the ListViews for the user to select the start and end dates from.
     * The listeners call the validateDates() method to ensure that the selected date range is valid.
     */
    private void addListeners() {
        from.valueProperty().addListener((observableValue, oldValue, newValue) -> validateDates());
        to.valueProperty().addListener((observableValue, oldValue, newValue) -> validateDates());
    }

    /**
     * Method to validate the selected dates.
     * <p>
     * This method validates the selected dates by checking if the start date is before the end date.
     * If the start date is after the end date, an error message is displayed to the user on the screen.
     * When the user corrects the dates, the error message is hidden.
     */
    private void validateDates() {
        errorMessage.setVisible(false);
        if (from.getValue() == null || to.getValue() == null) {
            return;
        }

        LocalDate fromDate = formatDate(from.getValue().toString());
        LocalDate toDate = formatDate(to.getValue().toString());

        if (!(fromDate.isBefore(toDate) || fromDate.equals(toDate))) {
            errorMessage.setVisible(true);
        }
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
            System.out.println("Error getting available dates \n" + e.getMessage() + "\n" + e.getCause() + "\n" + e.getStackTrace());
        }
    }

    /**
     * Method to set the backdrop for the welcome screen.
     * <p>
     * This method sets the backdrop for the welcome screen by loading the backdrop image from the resources folder.
     * The backdrop is then added to the screen as an ImageView.
     */
    private void setWelcomeBackdrop() {

        Image backdrop = new Image("backdrop.jpeg"); //placeholder image
        welcomeBackdrop.setImage(backdrop);

        welcomeBackdrop.setFitWidth(960);
        welcomeBackdrop.setFitHeight(600);
        welcomeBackdrop.setPreserveRatio(true);
        welcomeBackdrop.setVisible(true);
    }

    /**
     * Method to set the title for the welcome screen.
     * <p>
     * This method sets the title for the welcome screen by loading the title image from the resources folder.
     * The title is then added to the screen as an ImageView.
     */
    private void setGuiTitle() {
        guiTitle = new ImageView("file:src/resources/images/gui-title.png"); //doesnt exist yet!

        guiTitle.setFitWidth(500);
        guiTitle.setFitHeight(100);
        guiTitle.setPreserveRatio(false);
    }

    /**
     * Method to set the character for the welcome screen.
     * <p>
     * This method sets the character for the welcome screen by loading the character image from the resources folder.
     * The character is then added to the screen as an ImageView.
     */
    private void setGuiCharacter() {
        guiCharacter = new ImageView("file:src/resources/images/gui-character.png"); //doesnt exist yet!

        guiCharacter.setFitWidth(500);
        guiCharacter.setFitHeight(500);
        guiCharacter.setPreserveRatio(false);
    }

    /**
     * Method to show the welcome screen.
     * <p>
     * This method shows the welcome screen to the user by creating a new Stage and setting the scene to the welcome screen.
     * The stage is then displayed to the user.
     */
    public void showWelcomeScreen() {
        Stage welcomeStage = new Stage();
        welcomeStage.setTitle("Welcome to ??");
        welcomeStage.setResizable(false);

        Scene welcomeScene = new Scene(parent);

        welcomeStage.setScene(welcomeScene);
        welcomeStage.show();
    }

    /**
     * @return The start date selected by the user.
     */
    public LocalDate getFromDate() {
        return formatDate(from.getValue().toString());
    }

    /**
     * @return The end date selected by the user.
     */
    public LocalDate getToDate() {
        return formatDate(to.getValue().toString());
    }
}
