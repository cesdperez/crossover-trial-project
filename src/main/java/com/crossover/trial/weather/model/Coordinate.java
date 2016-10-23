package com.crossover.trial.weather.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static com.crossover.trial.weather.util.WeatherValidator.checkLatitude;
import static com.crossover.trial.weather.util.WeatherValidator.checkLongitude;

public class Coordinate {

    /**
     * earth radius in KM for calculating distances
     */
    private static final double EARTH_RADIUS_KM = 6372.8;

    /**
     * latitude value in degrees
     */
    private double latitude;

    /**
     * longitude value in degrees
     */
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        checkLatitude(latitude);
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        checkLongitude(longitude);
        this.longitude = longitude;
    }

    /**
     * Haversine distance between two coordinates.
     *
     * @return the distance in KM
     */
    public double distanceTo(Coordinate anotherCoordinate) {
        double deltaLat = Math.toRadians(anotherCoordinate.getLatitude() - this.getLatitude());
        double deltaLon = Math.toRadians(anotherCoordinate.getLongitude() - this.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(this.getLatitude()) * Math.cos(anotherCoordinate.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
