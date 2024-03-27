package gui.components;

import javafx.scene.chart.Chart;

import java.sql.ResultSet;

/**
 * An interface to plot charts.
 * <p>
 *
 * @author Enzo Bestetti (K23011872)
 * @version 2024.03.27
 */
public interface Plotter {

    /**
     * Plots the chart.
     *
     * @return the chart
     */
    Chart plot();

    /**
     * Sets the data to be plotted.
     *
     * @param data the data to be plotted
     */
    void setData(ResultSet data);

}
