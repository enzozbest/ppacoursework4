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

@SuppressWarnings({"all"})
public class BoroughController extends AbstractController {

    private final String boroughName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Stage boroughView;
    private ObservableList<CovidData> records;
    private Parent parent;
    private TableColumn date, retailAndRecreation, groceryAndPharmacy, parks, transitStations, workplaces, residential,
            newCases, totalCases, newDeaths, totalDeaths;
    @FXML
    private ImageView background;
    @FXML
    private Text close_button;
    @FXML
    private TableView table_view;
    @FXML
    private Label title;

    public BoroughController(String boroughName, LocalDate startDate, LocalDate endDate) {
        super("SELECT * FROM covid_london WHERE " +
                "`date` BETWEEN '" + startDate + "' AND '" + endDate + "' AND " +
                "borough=\"" + boroughName + "\" " +
                "ORDER BY `date` ASC;");

        this.boroughName = boroughName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.boroughView = new Stage();
        processQuery();
    }

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

        prepareScene();
        scene = new Scene(parent, 1450, 600, Paint.valueOf("white"));
        prepareStage();
        boroughView.show();
    }

    /**
     *
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


    private void prepareStage() {
        boroughView.setTitle("EpiQuest: " + boroughName);
        boroughView.setWidth(1450);
        boroughView.setHeight(560);
        boroughView.setResizable(false);
        boroughView.setScene(scene);
    }

    private void prepareScene() {
        setBackground();

        title.setText("Currently viewing: " + boroughName);

        setNavigationButton();
        setTableView();
        // populateTableView();
    }


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

        populateTableView();

        table_view.setItems(records);

        table_view.getColumns().addAll(date, retailAndRecreation, groceryAndPharmacy, parks, transitStations, workplaces,
                residential, newCases, totalCases, newDeaths, totalDeaths);


    }

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

    private void setBackground() {
        background.setImage(AssetLoader.WELCOME_BACKGROUND);
        background.setFitWidth(1450);
        background.setFitHeight(560);
        background.setPreserveRatio(false);
    }

    private void setNavigationButton() {
        hoverFlash(close_button);
        close_button.getStyleClass().add("clickable");
        close_button.setOnMouseClicked(event -> boroughView.close());
    }

}
