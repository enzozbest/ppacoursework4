package gui.components;

import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

/**
 * A class to plot line charts.
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
 * @see AbstractPlotter
 */
public class LinePlotter extends AbstractPlotter {

    private final LineChart<?, ?> chart;

    /**
     * Constructor for the LinePlotter class.
     *
     * @param title  the title of the chart
     * @param xAxis  the x-axis of the chart
     * @param xlabel the label of the x-axis
     * @param yAxis  the y-axis of the chart
     * @param ylabel the label of the y-axis
     */
    public LinePlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel) {
        super(title, xAxis, xlabel, yAxis, ylabel);
        chart = new LineChart<>(xAxis, yAxis);
    }

    /**
     * Plots the line chart by adding each piece of data to a Series, and then the Series to the chart.
     *
     * @return the line chart
     */
    @Override
    public Chart plot() {
        try {
            while (data.next()) {
                series.getData().add(new XYChart.Data<>(data.getString("month") + "/" + data.getString("year"), data.getInt("total_deaths")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null! " + e.getMessage() + e.getCause() + e.getStackTrace());
        }
        this.chart.getData().add(series);

        return this.chart;
    }
}