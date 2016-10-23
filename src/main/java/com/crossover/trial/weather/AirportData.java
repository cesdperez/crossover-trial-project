package com.crossover.trial.weather;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /**
     * the three letter IATA code
     */
    String iata;

    /**
     * latitude value in degrees
     */
    double latitude;

    /**
     * longitude value in degrees
     */
    double longitude;

    public AirportData() {
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("iata", iata)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportData that = (AirportData) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Objects.equals(iata, that.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata, latitude, longitude);
    }
}
