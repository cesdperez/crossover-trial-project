package com.crossover.trial.weather.service;

import com.crossover.trial.weather.model.AirportData;

/**
 * Services related to geographical calculations.
 */
public class GeoService {

    /**
     * earth radius in KM for calculating distances
     */
    private static final double EARTH_RADIUS_KM = 6372.8;

    /**
     * Singleton instance
     */
    private static final GeoService INSTANCE = new GeoService();

    public static GeoService getInstance() {
        return INSTANCE;
    }

    /**
     * Haversine distance between two coordinates.
     *
     * @return the distance in KM
     */
    public double calculateDistanceBetween(AirportData a1, AirportData a2) {
        double deltaLat = Math.toRadians(a2.getLatitude() - a1.getLatitude());
        double deltaLon = Math.toRadians(a2.getLongitude() - a1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(a1.getLatitude()) * Math.cos(a2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }

    public boolean areInRange(AirportData a1, AirportData a2, double radius) {
        return calculateDistanceBetween(a1, a2) <= radius;
    }
}
