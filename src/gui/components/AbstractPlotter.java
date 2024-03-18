package gui.components;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;

import java.sql.ResultSet;

public abstract class AbstractPlotter implements Plotter{

    protected final Axis xAxis;
    protected final Axis yAxis;
    protected ResultSet data;
    protected final XYChart.Series series;
    protected Chart chart;

    protected AbstractPlotter(String title, Axis xAxis, String xlabel, Axis yAxis, String ylabel){
        this.xAxis = xAxis;
        this.xAxis.setLabel(xlabel);

        this.yAxis = yAxis;
        this.yAxis.setLabel(ylabel);

        series = new XYChart.Series();
    }

    @Override
    public void setData(ResultSet data){
        this.data = data;
    }


}
