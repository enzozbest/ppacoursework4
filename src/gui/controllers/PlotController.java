package gui.controllers;

import gui.components.Plotter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.ResultSet;

/**
 * This class is a controller for the plot-frame.fxml file. It is used to display the plot of the data
 * that is passed to it.
 * <p>
 * The class extends the AbstractController class and uses the data that is passed to it to display the plot
 * in a new window.
 * <p>
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.18
 */
public class PlotController extends AbstractController {


    private BorderPane parent;

    @FXML
    private Label title;

    private Stage stage;

    /**
     * The constructor for the PlotController class. It takes a string as a parameter and passes it to the
     * super class constructor.
     *
     * @param queryString the query string that is used to get the data from the database.
     */
    public PlotController(String queryString) {
        super(queryString);
    }

    /**
     * This method loads the FXML file for the plot-frame and sets the controller for the file to this class.
     *
     * @return the parent node of the FXML file.
     */
    @Override
    public void beginLoading() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxml/plot-frame.fxml"));
        loader.setController(this);

        try {
            parent = loader.load();
        } catch (Exception e) {
            System.out.println("Error loading the FXML file for the plot-frame! \n" + e.getCause() + "\n" + e.getMessage() + "\n" + e.getStackTrace());
        }
    }

    /**
     * This method is called when the close button is clicked. It closes the stage that the plot is displayed in.
     */
    @FXML
    private void closeButton() {
        if (stage == null) {
            return;
        }
        stage.close();
    }

    /**
     * This method is called to display the plot of the data that is passed to it. It creates a new stage and
     * sets the scene to the plot that is created by the plotter class.
     *
     * @param plotter the plotter object that is used to create the plot.
     */
    @FXML
    public void showGraph(Plotter plotter) {
        beginLoading();
        stage = new Stage();

        plotter.setData(data);
        Chart centerNode = plotter.plot();
        parent.setCenter(centerNode);


        stage.setTitle(centerNode.getTitle());

        Scene scene = new Scene(parent, 960, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @return the data that is passed to the plotter for use elsewhere in the application.
     */
    public ResultSet getData() {
        return data;
    }
}
