package gui.components;

import javafx.scene.chart.Chart;

import java.sql.ResultSet;

/**
 * An interface to plot charts.
 * <p>
 *
 * @author Enzo Bestetti
 * @version 2024.03.18
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
