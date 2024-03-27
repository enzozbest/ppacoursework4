package utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * This record class represents one record in the London Covid-19 dataset.
 * This is essentially one row in the data table as a Java object. Each column of the table
 * has a corresponding field.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.27
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

    /**
     * Constructor for the CovidData class.
     *
     * @param date                The date of the record.
     * @param borough             The borough of the record.
     * @param retailAndRecreation The retail and recreation mobility value.
     * @param groceryAndPharmacy  The grocery and pharmacy mobility value.
     * @param parks               The parks mobility value.
     * @param transitStations     The transit stations mobility value.
     * @param workplaces          The workplaces mobility value.
     * @param residential         The residential mobility value.
     * @param newCases            The number of new cases.
     * @param totalCases          The total number of cases.
     * @param newDeaths           The number of new deaths.
     * @param totalDeaths         The total number of deaths.
     */
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

    /**
     * @return The date of the record as a StringProperty.
     */
    public String getDateProperty() {
        return dateProperty.get();
    }

    /**
     * @return The borough of the record as a StringProperty.
     */
    public String getBoroughProperty() {
        return boroughProperty.get();
    }

    /**
     * @return The retail and recreation mobility value as an IntegerProperty.
     */
    public Integer getRetailAndRecreationProperty() {
        return retailAndRecreationProperty.get();
    }

    /**
     * @return The grocery and pharmacy mobility value as an IntegerProperty.
     */
    public Integer getGroceryAndPharmacyProperty() {
        return groceryAndPharmacyProperty.get();
    }

    /**
     * @return The parks mobility value as an IntegerProperty.
     */
    public Integer getParksProperty() {
        return parksProperty.get();
    }

    /**
     * @return The transit stations mobility value as an IntegerProperty.
     */
    public Integer getTransitStationProperty() {
        return transitStationProperty.get();
    }

    /**
     * @return The workplaces mobility value as an IntegerProperty.
     */
    public Integer getWorkplacesProperty() {
        return workplacesProperty.get();
    }

    /**
     * @return The residential mobility value as an IntegerProperty.
     */
    public Integer getResidentialProperty() {
        return residentialProperty.get();
    }

    /**
     * @return The number of new cases as an IntegerProperty.
     */
    public Integer getNewCasesProperty() {
        return newCasesProperty.get();
    }

    /**
     * @return The total number of cases as an IntegerProperty.
     */
    public Integer getTotalCasesProperty() {
        return totalCasesProperty.get();
    }

    /**
     * @return The number of new deaths as an IntegerProperty.
     */
    public Integer getNewDeathsProperty() {
        return newDeathsProperty.get();
    }

    /**
     * @return The total number of deaths as an IntegerProperty.
     */
    public Integer getTotalDeathsProperty() {
        return totalDeathsProperty.get();
    }

    /**
     * @return The date of the record as a String.
     */
    public String date() {
        return date;
    }

    /**
     * @return The borough of the record as a String.
     */
    public String borough() {
        return borough;
    }

    /**
     * @return The retail and recreation mobility value as an Integer.
     */
    public Integer retailAndRecreation() {
        return retailAndRecreation;
    }

    /**
     * @return The grocery and pharmacy mobility value as an Integer.
     */
    public Integer groceryAndPharmacy() {
        return groceryAndPharmacy;
    }

    /**
     * @return The parks mobility value as an Integer.
     */
    public Integer parks() {
        return parks;
    }

    /**
     * @return The transit stations mobility value as an Integer.
     */
    public Integer transitStations() {
        return transitStations;
    }

    /**
     * @return The workplaces mobility value as an Integer.
     */
    public Integer workplaces() {
        return workplaces;
    }

    /**
     * @return The residential mobility value as an Integer.
     */
    public Integer residential() {
        return residential;
    }

    /**
     * @return The number of new cases as an Integer.
     */
    public Integer newCases() {
        return newCases;
    }

    /**
     * @return The total number of cases as an Integer.
     */
    public Integer totalCases() {
        return totalCases;
    }

    /**
     * @return The number of new deaths as an Integer.
     */
    public Integer newDeaths() {
        return newDeaths;
    }

    /**
     * @return The total number of deaths as an Integer.
     */
    public Integer totalDeaths() {
        return totalDeaths;
    }
}

