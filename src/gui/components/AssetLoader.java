package gui.components;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Class to load images for the application.
 * <p>
 * Images are loaded as Image objects and are stored as in global fields. This allows the images to be
 * accessed from any class in the application.
 * <p>
 * The images are loaded only once, and then stored in memory for the lifetime of the application.
 * This reduces the amount of memory used by the application and also reduces the overhead due to construction
 * and garbage collection of Image objects.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.28
 */
public class AssetLoader {

    /**
     * Image object for Welcome background.
     */
    public static final Image WELCOME_BACKGROUND = new Image("images/welcome_background.png");
    /**
     * Image object for the Statistics background.
     */
    public static final Image STATS_BACKGROUND = new Image("images/stats_background.png");
    /**
     * Image object for the Graph background.
     */
    public static final Image GRAPH_BACKGROUND = new Image("images/graph_background.png");
    /**
     * Image object for the London Map frame.
     */
    public static final Image MAP_FRAME = new Image("images/map_frame.png");
    /**
     * Image object for the title on the main screen.
     */
    public static final Image WELCOME_TITLE = new Image("images/welcome_title.png");
    /**
     * Image object for "enter valid date range" subtitle on the main screen.
     */
    public static final Image ENTER_DATE_RANGE = new Image("images/enter_date_range.png");
    /**
     * Image object for "click here to start" subtitle on the main screen.
     */
    public static final Image PRESS_TO_START = new Image("images/click_to_begin.png");
    /**
     * Image object for the "invalid date range :( ) text on the main screen.
     */
    public static final Image INVALID_DATE_RANGE = new Image("images/invalid_date_range.png");
    /**
     * Image for the "select location" text.
     */
    public static final Image SELECT_LOCATION = new Image("images/select_location.png");
    /**
     * Image object for the right arrow.
     */
    public static final Image ARROW = new Image("images/forward_arrow.png");
    /**
     * Image object for character on the Welcome screen.
     */
    public static final Image CHARACTER = new Image("images/character.gif");
    /**
     * Image object for the small character
     */
    public static final Image SMALL_CHARACTER = new Image("images/small_character.gif");

    static {
        // The font must be loaded in a static block so that it is loaded only once.
        // This must be loaded so that the CSS styling works correctly.
        Font.loadFont("file:/home/enzozbest/IdeaProjects/ppacoursework4/src/resources/fonts/font.ttf", 30);
    }
}
