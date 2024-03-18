package gui.components;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("all")
public class BarChartPlotter extends AbstractPlotter{

    private final BarChart chart;

    public BarChartPlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel){
        super(title, xAxis, xlabel, yAxis, ylabel);
        this.chart = new BarChart<>(xAxis, yAxis);
    }

    @Override
    public Scene plot() {
        try {
            while(data.next()){
                series.getData().add(new XYChart.Data<>(data.getString("borough"), data.getInt("total_cases")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null! ");
        }
        this.chart.getData().add(series);

        return new Scene(chart, 960, 600);
    }
}
