package gui.components;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

public class LinePlotter extends AbstractPlotter {

    private final LineChart<?, ?> chart;

    public LinePlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel) {
        super(title, xAxis, xlabel, yAxis, ylabel);
        chart = new LineChart<>(xAxis, yAxis);
    }

    @Override
    public Scene plot() {
        try {
            while(data.next()){
                series.getData().add(new XYChart.Data<>(data.getString("date"), data.getInt("total_cases")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null! ");
        }
        this.chart.getData().add(series);

        return new Scene(chart, 960, 600);
    }
}