package gui.components;


import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * This class is used to load images for the application.
 * <p>
 * The images are loaded as Image objects and are stored as public static final fields. This allows the images to be
 * accessed from any class in the application. The images are loaded from the resources folder in the project only once,
 * and then stored in memory for the lifetime of the application. This reduces the amount of memory used by the
 * application and also reduces overhead due to construction and garbage collection of image objects.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.20
 */
public class AssetLoader {

    /**
     * Image object for background image.
     */
    public static final Image WELCOME_BACKDROP = new Image("backdrop.png");

    /**
     * Image object for the title on the main screen.
     */
    public static final Image WELCOME_TITLE = new Image("title.png");

    /**
     * Image object for ENTER_DATE_RANGE subtitle on the main screen.
     */
    public static final Image ENTER_DATE_RANGE = new Image("enter_date_range.png");

    /**
     * Image object for "PRESS TO START" subtitle on the main screen.
     */
    public static final Image PRESS_TO_START = new Image("click_to_begin.png");

    /**
     * Image object for selector GIF shown on hover on the main screen.
     */
    public static final Image INVALID_DATE_RANGE = new Image("invalid_date_range.png");

    /**
     * Image object for character on the main screen.
     */
    public static final Image WELCOME_CHARACTER = new Image("character.gif");

    /**
     * Image object for the back text.
     */
    public static final Image BACK = new Image("back.png");

    public static final Image FORWARD = new Image("forward.png");

    /**
     * Image object for the map frame.
     */
    public static final Image MAP_FRAME = new Image("map_frame.png");

    /**
     * Image object for the map.
     */
    public static final Image MAP = new Image("map.png");

    public static final Image STATS_BACKGROUND = new Image("stats_background.png");

    public static final Font EQ_FONT = Font.loadFont("file:/home/enzozbest/IdeaProjects/ppacoursework4/src/resources/fonts/font.ttf", 30);

    // public static final Image GRAPH_BACKDROP = new Image("graph_backdrop.png");

}
