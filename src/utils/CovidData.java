package utils;

/**
 * This record class represents one record in the COVID dataset.
 * This is essentially one row in the data table. Each column of the table
 * has a corresponding field.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902), Jacelyne Tan (K?)
 * @version 2024.03.13
 */

public record CovidData(String date, String borough, int retailAndRecreation, int groceryAndPharmacy, int parks,
                        int transitStations, int workplaces, int residential, int newCases, int totalCases,
                        int newDeaths, int totalDeaths) {
}