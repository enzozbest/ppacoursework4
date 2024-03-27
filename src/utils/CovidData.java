package utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * This record class represents one record in the COVID dataset.
 * This is essentially one row in the data table. Each column of the table
 * has a corresponding field.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K?)
 * @version 2024.03.13
 */

public class CovidData {


    private final StringProperty dateProperty;
    private final StringProperty boroughProperty;
    private final IntegerProperty retailAndRecreationProperty;
    private final IntegerProperty groceryAndPharmacyProperty;
    private final IntegerProperty parksProperty;
    private final IntegerProperty transitStationProperty;
    private final IntegerProperty workplacesProperty;
    private final IntegerProperty residentialProperty;
    private final IntegerProperty newCasesProperty;
    private final IntegerProperty totalCasesProperty;
    private final IntegerProperty newDeathsProperty;
    private final IntegerProperty totalDeathsProperty;
    private final String date;
    private final Integer retailAndRecreation;
    private final Integer groceryAndPharmacy;
    private final Integer parks;
    private final Integer transitStations;
    private final Integer workplaces;
    private final Integer residential;
    private final Integer newCases;
    private final Integer totalCases;
    private final Integer newDeaths;
    private final Integer totalDeaths;
    private String borough;

    public CovidData(String date, String borough, Integer retailAndRecreation, Integer groceryAndPharmacy, Integer parks,
                     Integer transitStations, Integer workplaces, Integer residential, Integer newCases, Integer totalCases,
                     Integer newDeaths, Integer totalDeaths) {

        this.date = date;
        this.retailAndRecreation = retailAndRecreation;
        this.groceryAndPharmacy = groceryAndPharmacy;
        this.parks = parks;
        this.transitStations = transitStations;
        this.workplaces = workplaces;
        this.residential = residential;
        this.newCases = newCases;
        this.totalCases = totalCases;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;


        dateProperty = new SimpleStringProperty(date);
        boroughProperty = new SimpleStringProperty(borough);
        retailAndRecreationProperty = new SimpleIntegerProperty(retailAndRecreation);
        groceryAndPharmacyProperty = new SimpleIntegerProperty(groceryAndPharmacy);
        parksProperty = new SimpleIntegerProperty(parks);
        transitStationProperty = new SimpleIntegerProperty(transitStations);
        workplacesProperty = new SimpleIntegerProperty(workplaces);
        residentialProperty = new SimpleIntegerProperty(residential);
        newCasesProperty = new SimpleIntegerProperty(newCases);
        totalCasesProperty = new SimpleIntegerProperty(totalCases);
        newDeathsProperty = new SimpleIntegerProperty(newDeaths);
        totalDeathsProperty = new SimpleIntegerProperty(totalDeaths);
    }

    public String getDateProperty() {
        return dateProperty.get();
    }

    public String getBoroughProperty() {
        return boroughProperty.get();
    }

    public Integer getRetailAndRecreationProperty() {
        return retailAndRecreationProperty.get();
    }

    public Integer getGroceryAndPharmacyProperty() {
        return groceryAndPharmacyProperty.get();
    }

    public Integer getParksProperty() {
        return parksProperty.get();
    }

    public Integer getTransitStationProperty() {
        return transitStationProperty.get();
    }

    public Integer getWorkplacesProperty() {
        return workplacesProperty.get();
    }

    public Integer getResidentialProperty() {
        return residentialProperty.get();
    }

    public Integer getNewCasesProperty() {
        return newCasesProperty.get();
    }

    public Integer getTotalCasesProperty() {
        return totalCasesProperty.get();
    }

    public Integer getNewDeathsProperty() {
        return newDeathsProperty.get();
    }

    public Integer getTotalDeathsProperty() {
        return totalDeathsProperty.get();
    }

    public String date() {
        return date;
    }

    public Integer retailAndRecreation() {
        return retailAndRecreation;
    }

    public Integer groceryAndPharmacy() {
        return groceryAndPharmacy;
    }

    public Integer parks() {
        return parks;
    }

    public Integer transitStations() {
        return transitStations;
    }

    public Integer workplaces() {
        return workplaces;
    }

    public Integer residential() {
        return residential;
    }

    public Integer newCases() {
        return newCases;
    }

    public Integer totalCases() {
        return totalCases;
    }

    public Integer newDeaths() {
        return newDeaths;
    }

    public Integer totalDeaths() {
        return totalDeaths;
    }

    public String borough() {
        return borough;
    }
}

