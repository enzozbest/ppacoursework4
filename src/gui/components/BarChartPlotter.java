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
 * The data is passed to the plotter as a ResultSet, and the plot method is called to plot the bar chart.
 * The bar chart is then returned to the caller.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
 */
@SuppressWarnings("rawtypes, unchecked")
public class BarChartPlotter extends AbstractPlotter {

    private final BarChart chart;

    /**
     * Constructor for the BarChartPlotter class.
     *
     * @param title  the title of the chart
     * @param xAxis  the x-axis of the chart
     * @param xlabel the label of the x-axis
     * @param yAxis  the y-axis of the chart
     * @param ylabel the label of the y-axis
     */
    public BarChartPlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel) {
        super(title, xAxis, xlabel, yAxis, ylabel);
        this.chart = new BarChart<>(xAxis, yAxis);
    }

    /**
     * This method is used to plot the data as a bar chart.
     * It iterates over the <it>ResultSet</it> and adds each piece of data to the plot.
     *
     * @return the chart with the data plotted
     */
    @Override
    public Chart plot() {
        try {
            while (data.next()) {
                series.getData().add(new XYChart.Data<>(data.getString("borough"), data.getInt("average_statistic")));
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("The data passed to the plotter is null! ");
        }

        this.chart.getData().add(series);

        return this.chart;
    }
}
