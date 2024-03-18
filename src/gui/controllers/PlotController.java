package gui.controllers;

import gui.PlotTest;
import gui.components.Plotter;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import utils.sql.queries.Query;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class PlotController extends AbstractController implements Initializable {


    public PlotController(String queryString){
        super(queryString);
    }

    public ResultSet getData(){
        return data;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
