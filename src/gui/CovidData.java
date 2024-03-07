package gui;

/**
 * Represents one record in the COVID dataset.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 */

public class CovidData {

    /**
     * The date the COVID information (cases & deaths) was collected
     */
    private final String date;

    /**
     * The COVID information is organised by (London) borough
     */
    private final String borough;


    /**
     * The COVID information that's collected daily for each London borough
     */
    private final int newCases;
    private final int totalCases;
    private final int newDeaths;
    private final int totalDeaths;


    /**
     * Google analysed location data from Android smartphones to measure movement
     * in London.  The data shows percent change from the baseline.  For example,
     * a negative value means there's less human traffic compared to the baseline.
     */
    private final int retailRecreationGMR;
    private final int groceryPharmacyGMR;
    private final int parksGMR;
    private final int transitGMR;
    private final int workplacesGMR;
    private final int residentialGMR;

    /**
     * Constructor for gui.CovidData
     *
     * @param date
     * @param borough
     * @param retailRecreationGMR
     * @param groceryPharmacyGMR
     * @param parksGMR
     * @param transitGMR
     * @param workplacesGMR
     * @param residentialGMR
     * @param newCases
     * @param totalCases
     * @param newDeaths
     * @param totalDeaths
     */
    public CovidData(String date, String borough, int retailRecreationGMR, int groceryPharmacyGMR,
                     int parksGMR, int transitGMR, int workplacesGMR, int residentialGMR,
                     int newCases, int totalCases, int newDeaths, int totalDeaths) {

        this.date = date;
        this.borough = borough;
        this.retailRecreationGMR = retailRecreationGMR;
        this.groceryPharmacyGMR = groceryPharmacyGMR;
        this.parksGMR = parksGMR;
        this.transitGMR = transitGMR;
        this.workplacesGMR = workplacesGMR;
        this.residentialGMR = residentialGMR;
        this.newCases = newCases;
        this.totalCases = totalCases;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
    }


    public String getDate() {
        return date;
    }


    public String getBorough() {
        return borough;
    }


    public int getRetailRecreationGMR() {
        return retailRecreationGMR;
    }


    public int getGroceryPharmacyGMR() {
        return groceryPharmacyGMR;
    }


    public int getParksGMR() {
        return parksGMR;
    }


    public int getTransitGMR() {
        return transitGMR;
    }


    public int getWorkplacesGMR() {
        return workplacesGMR;
    }


    public int getResidentialGMR() {
        return residentialGMR;
    }


    public int getNewCases() {
        return newCases;
    }


    public int getTotalCases() {
        return totalCases;
    }


    public int getNewDeaths() {
        return newDeaths;
    }


    public int getTotalDeaths() {
        return totalDeaths;
    }


}
