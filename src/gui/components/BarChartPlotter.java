package gui.components;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

/**
 * This class is used to plot a bar chart.
 * <p>
 * The BarChartPlotter class is used to plot a bar chart with the data passed to it.
 * The data is passed to the plotter as a ResultSet, and the plot method plots the bar chart.
 * The bar chart is then returned to the caller.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K23085324)
 * @version 2024.03.27
 */
public class BarChartPlotter extends AbstractPlotter {

    private final BarChart<String, Number> chart;

    /**
     * Constructor for the BarChartPlotter class.
     *
     * @param xAxis  the x-axis of the chart
     * @param xLabel the label of the x-axis
     * @param yAxis  the y-axis of the chart
     * @param yLabel the label of the y-axis
     */
    public BarChartPlotter(Axis<String> xAxis, String xLabel, Axis<Number> yAxis, String yLabel) {
        super(xAxis, xLabel, yAxis, yLabel);
        this.chart = new BarChart<>(xAxis, yAxis);
    }

    /**
     * This method is used to plot the data as a bar chart.
     * It iterates over the ResultSet and adds each piece of data to the plot.
     *
     * @return the chart with the data plotted
     */
    @Override
    public Chart plot() {
        try {
            while (data.next()) {
                super.series.getData().add(new XYChart.Data<>(data.getString("borough"),
                        data.getInt("average_statistic")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null!" + e.getMessage() + e.getCause() + e.getStackTrace());
        }

        this.chart.getData().add(super.series);

        return this.chart;
    }
}
