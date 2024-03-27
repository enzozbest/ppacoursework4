package gui.controllers;

import gui.components.AssetLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.CovidData;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * BoroughController class is responsible for displaying all the data of a specific borough.
 * <p>
 * This class is used to control the screen shown to the user after they click "View Borough" on the Map screen.
 * It displays a table of data for a specific borough in a table format, showing the user the data for each day
 * in the specified date range.
 * <p>
 * The user is able to order the data in the table by clicking on the column headers.
 * <p>
 * CSS styles are applied to the table to make it more visually appealing and to make the Borough View screen consistent
 * with the design of the rest of the application.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
 */
@SuppressWarnings("rawtypes, unchecked")
public class BoroughController extends AbstractController {

    private final String boroughName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Stage boroughView;
    @FXML
    private ImageView background;
    @FXML
    private Text close_button;
    @FXML
    private TableView table_view;
    @FXML
    private Label title;
    private ObservableList<CovidData> records;
    private Parent parent;
    private TableColumn date, retailAndRecreation, groceryAndPharmacy, parks, transitStations, workplaces, residential,
            newCases, totalCases, newDeaths, totalDeaths;

    /**
     * Constructor for the BoroughController class.
     *
     * @param boroughName The name of the borough to display data for.
     * @param startDate   The start date of the date range to display data for.
     * @param endDate     The end date of the date range to display data for.
     */
    public BoroughController(String boroughName, LocalDate startDate, LocalDate endDate) {
        super("SELECT * FROM covid_london WHERE " +
                "`date` BETWEEN '" + startDate + "' AND '" + endDate + "' AND " +
                "borough=\"" + boroughName + "\" " +
                "ORDER BY `date` ASC;");

        this.boroughName = boroughName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.boroughView = new Stage();
        this.processQuery();
    }

    /**
     * Method to begin loading the BoroughController.
     * <p>
     * This method is called when the user clicks "View Borough" on the Map screen.
     * It loads the FXML file for the Borough View screen and sets up the scene and stage.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/borough-view.fxml"));
        loader.setController(this);
        try {
            parent = loader.load();
        } catch (IOException e) {
            System.out.println("E");
        }

        parent.getStylesheets().add(getClass().getResource("../../resources/styles/table-view.css").toExternalForm());
        parent.getStylesheets().add(getClass().getResource("../../resources/styles/default.css").toExternalForm());

        this.prepareScene();
        scene = new Scene(parent, 1450, 600, Paint.valueOf("white"));

        this.prepareStage();

        boroughView.show();
    }

    /**
     * Method to prepare the stage for the Borough View screen.
     * <p>
     * This method is called when the BoroughController is created.
     * It sets the title, width, height, and scene for the stage.
     */
    private void prepareStage() {
        boroughView.setTitle("EpiQuest: " + boroughName);
        boroughView.setWidth(1450);
        boroughView.setHeight(560);
        boroughView.setResizable(false);
        boroughView.setScene(scene);
    }

    /**
     * Method to prepare the scene for the Borough View screen.
     * <p>
     * This method is called when the BoroughController is created.
     * It sets the background, title, navigation button, and table view for the scene.
     */
    private void prepareScene() {
        this.setBackground();
        title.setText("Currently viewing: " + boroughName);
        this.setNavigationButton();
        this.setTableView();
    }

    /**
     * Method to set up the TableView for the Borough View screen.
     * <p>
     * This method is called when the BoroughController is created.
     * It sets up the TableView with the columns for each data field, and populates the TableView with the CovidData objects.
     * The TableView is made editable so that the user can order the data by clicking on the column headers.
     * The TableView is styled using CSS to make it visually appealing and consistent with the design of the rest of the application.
     */
    private void setTableView() {
        table_view.setEditable(true);

        table_view.setBackground(Background.EMPTY);

        date = new TableColumn("Date");
        date.setMinWidth(50);

        retailAndRecreation = new TableColumn("Retail and Recreation");
        retailAndRecreation.setMinWidth(190);

        groceryAndPharmacy = new TableColumn("Grocery and Pharmacy");
        groceryAndPharmacy.setMinWidth(200);

        parks = new TableColumn("Parks");
        parks.setMinWidth(60);

        transitStations = new TableColumn("Transit Stations");
        transitStations.setMinWidth(160);

        workplaces = new TableColumn("Workplaces");
        workplaces.setMinWidth(100);

        residential = new TableColumn("Residential");
        residential.setMinWidth(100);

        newCases = new TableColumn("New Cases");
        newCases.setMinWidth(100);

        totalCases = new TableColumn("Total Cases");
        totalCases.setMinWidth(120);

        newDeaths = new TableColumn("New Deaths");
        newDeaths.setMinWidth(100);

        totalDeaths = new TableColumn("Total Deaths");
        totalDeaths.setMinWidth(120);

        this.populateTableView();

        table_view.setItems(records);

        table_view.getColumns().addAll(date, retailAndRecreation, groceryAndPharmacy, parks, transitStations, workplaces,
                residential, newCases, totalCases, newDeaths, totalDeaths);
    }

    /**
     * Method to populate the TableView with CovidData objects.
     * <p>
     * This method is called when the BoroughController is created.
     * It populates the TableView with the CovidData objects generated from the database query.
     * The PropertyValueFactory is used to bind the data fields of the CovidData objects to the columns in the TableView.
     * This allows the TableView to display the data in the CovidData objects in the correct columns.
     * The PropertyValueFactory also allows the TableView to be editable, so that the user can order the data by clicking on the column headers.
     * The TableView is styled using CSS to make it visually appealing and consistent with the design of the rest of the application.
     */
    private void populateTableView() {
        date.setCellValueFactory(new PropertyValueFactory<CovidData, String>("dateProperty"));
        retailAndRecreation.setCellValueFactory(new PropertyValueFactory<CovidData, String>("retailAndRecreationProperty"));
        groceryAndPharmacy.setCellValueFactory(new PropertyValueFactory<CovidData, String>("groceryAndPharmacyProperty"));
        parks.setCellValueFactory(new PropertyValueFactory<CovidData, String>("parksProperty"));
        transitStations.setCellValueFactory(new PropertyValueFactory<CovidData, String>("transitStationProperty"));
        workplaces.setCellValueFactory(new PropertyValueFactory<CovidData, String>("workplacesProperty"));
        residential.setCellValueFactory(new PropertyValueFactory<CovidData, String>("residentialProperty"));
        newCases.setCellValueFactory(new PropertyValueFactory<CovidData, String>("newCasesProperty"));
        totalCases.setCellValueFactory(new PropertyValueFactory<CovidData, String>("totalCasesProperty"));
        newDeaths.setCellValueFactory(new PropertyValueFactory<CovidData, String>("newDeathsProperty"));
        totalDeaths.setCellValueFactory(new PropertyValueFactory<CovidData, String>("totalDeathsProperty"));
    }

    /**
     * Method to set the background image for the Borough View screen.
     * <p>
     * This method is called when the BoroughController is created.
     * It sets the background image for the scene to the image specified in the AssetLoader class.
     */
    private void setBackground() {
        background.setImage(AssetLoader.WELCOME_BACKGROUND);
        background.setFitWidth(1450);
        background.setFitHeight(560);
        background.setPreserveRatio(false);
    }

    /**
     * Method to set the navigation button for the Borough View screen.
     * <p>
     * This method is called when the BoroughController is created.
     * It sets the navigation button for the scene to the image specified in the AssetLoader class.
     */
    private void setNavigationButton() {
        super.hoverFlash(close_button);
        close_button.getStyleClass().add("clickable");
        close_button.setOnMouseClicked(event -> boroughView.close());
    }

    /**
     * Method to process the database query and generate CovidData objects.
     * <p>
     * This method is called when the BoroughController is created.
     * It processes the database query and generates CovidData objects for each row in the ResultSet.
     * The CovidData objects are stored in an ObservableList, which is used to populate the TableView.
     */
    private void processQuery() {
        records = FXCollections.observableArrayList();
        try {
            while (data.next()) {
                records.add(new CovidData(data.getString("date"), data.getString("borough"), data.getInt("retail_and_recreation"),
                        data.getInt("grocery_and_pharmacy"), data.getInt("parks"), data.getInt("transit_stations"),
                        data.getInt("workplaces"), data.getInt("residential"), data.getInt("new_cases"),
                        data.getInt("total_cases"), data.getInt("new_deaths"), data.getInt("total_deaths")));
            }
        } catch (SQLException e) {
            System.out.println("Error generating covid records");
        }
    }
}
