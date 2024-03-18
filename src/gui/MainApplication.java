package gui;

import gui.components.BarChartPlotter;
import gui.components.LinePlotter;
import gui.components.Plotter;
import gui.controllers.PlotController;
import gui.controllers.ProvisionalController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.sql.queries.Query;
import utils.sql.queries.concurrent.QueryExecutor;

@SuppressWarnings("all")
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ProvisionalController controller = new ProvisionalController();
        Scene scene = new Scene(controller.beginLoading(), 960, 600);

        PlotController plotController = new PlotController("SELECT borough, total_cases FROM covid_london WHERE date = '2022-10-15' ORDER BY borough;");
        Plotter bcPlotter = new BarChartPlotter("CV-19 Boroughs", new CategoryAxis(), "Boroughs", new NumberAxis(), "Number of Cases");
        bcPlotter.setData(plotController.getData());

        PlotController lineController = new PlotController("SELECT `date`, total_cases FROM covid_london WHERE borough='Greenwich' ORDER BY MONTH(`date`);");
        Plotter linePlotter = new LinePlotter("Greenwich over Time", new CategoryAxis(), "Date", new NumberAxis(), "Total cases");
        linePlotter.setData(lineController.getData());

        Scene plotScene = bcPlotter.plot();
        Scene lineScene = linePlotter.plot();

        stage.setScene(lineScene);
        // stage.getIcons().add(new Image(<path to image>)) to change the icon of the window
        stage.show();
    }
}



