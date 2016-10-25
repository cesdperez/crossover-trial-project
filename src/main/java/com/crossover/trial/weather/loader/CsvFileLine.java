package com.crossover.trial.weather.loader;

/**
 * Represents a line of a CSV file that is read by the {@link CsvReader}
 */
final class CsvFileLine {

    final String iataCode;
    final String longitude;
    final String latitude;

    CsvFileLine(String iataCode, String longitude, String latitude) {
        this.iataCode = iataCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CsvFileLine{");
        sb.append("iataCode='").append(iataCode).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
