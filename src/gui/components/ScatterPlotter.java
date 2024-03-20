package gui.components;

import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

/**
 * This class is a concrete implementation of the AbstractPlotter class. It is used
 * to create a ScatterChart object and plot the data passed to it.
 * <p>
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
 */
@SuppressWarnings("all")
public class ScatterPlotter extends AbstractPlotter {
    private final ScatterChart<?, ?> chart;

    /**
     * Constructor for the ScatterPlotter class.
     *
     * @param title  The title of the chart
     * @param xAxis  The x-axis of the chart
     * @param xlabel The label of the x-axis
     * @param yAxis  The y-axis of the chart
     * @param ylabel The label of the y-axis
     */
    public ScatterPlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel) {
        super(title, xAxis, xlabel, yAxis, ylabel);
        chart = new ScatterChart<Object, Object>(xAxis, yAxis);
    }

    /**
     * This method is used to plot the data passed to the ScatterPlotter object.
     *
     * @return The ScatterChart object with the data plotted
     */
    @Override
    public Chart plot() {
        try {
            while (data.next()) {
                series.getData().add(new XYChart.Data<>(data.getString("date"), data.getInt("total_cases")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null! ");
        }
        this.chart.getData().add(series);

        return this.chart;
    }
}
