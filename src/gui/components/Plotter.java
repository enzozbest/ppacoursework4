package gui.components;

import javafx.scene.Scene;

import java.sql.ResultSet;

public interface Plotter {

    public Scene plot();
    public void setData(ResultSet data);

}
