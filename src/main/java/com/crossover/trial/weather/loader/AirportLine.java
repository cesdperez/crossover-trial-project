package com.crossover.trial.weather.loader;

/**
 * Represents a line of a CSV file that is read by the {@link CsvReader}
 */
final class AirportLine {

    final String iataCode;
    final String longitude;
    final String latitude;

    AirportLine(String iataCode, String longitude, String latitude) {
        this.iataCode = iataCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
