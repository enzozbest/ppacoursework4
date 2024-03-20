package gui.components;

import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;

import java.sql.ResultSet;

@SuppressWarnings("unused")
/**
 * Abstract class for plotting data.
 * This class is used to create a plotter for a specific type of chart.
 * It contains the basic methods and attributes that are common to all types of plotters.
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
 * @see Plotter
 */
public abstract class AbstractPlotter implements Plotter {

    protected final Axis xAxis;
    protected final Axis yAxis;
    protected final XYChart.Series series;
    protected ResultSet data;
    protected Chart chart;

    /**
     * Constructor for the AbstractPlotter class.
     * It initializes the x and y axes and the series of the chart.
     *
     * @param title  the title of the chart
     * @param xAxis  the x-axis of the chart
     * @param xlabel the label of the x-axis
     * @param yAxis  the y-axis of the chart
     * @param ylabel the label of the y-axis
     */
    protected AbstractPlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel) {
        this.xAxis = xAxis;
        this.xAxis.setLabel(xlabel);

        this.yAxis = yAxis;
        this.yAxis.setLabel(ylabel);

        series = new XYChart.Series();
    }

    /**
     * Define the data to be plotted.
     *
     * @param data a ResultSet object containing the data to be plotted
     */
    @Override
    public void setData(ResultSet data) {
        this.data = data;
    }
}
