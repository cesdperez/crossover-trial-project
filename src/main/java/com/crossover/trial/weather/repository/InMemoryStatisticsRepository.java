package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.model.AirportData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory repository for Airport Weather Domain statistics that supports concurrency.
 */
public class InMemoryStatisticsRepository {

    /**
     * Internal performance counter to better understand most requested information
     */
    private Map<AirportData, AtomicInteger> requestFrequency;

    /**
     * Performance counter to measure radius
     */
    private Map<Double, AtomicInteger> radiusFrequency;

    /**
     * Singleton instance
     */
    private static final InMemoryStatisticsRepository INSTANCE = new InMemoryStatisticsRepository();

    public InMemoryStatisticsRepository() {
        requestFrequency = new ConcurrentHashMap<>();
        radiusFrequency = new ConcurrentHashMap<>();
    }

    public Map<AirportData, AtomicInteger> getRequestFrequency() {
        return requestFrequency;
    }

    public Map<Double, AtomicInteger> getRadiusFrequency() {
        return radiusFrequency;
    }

    public static InMemoryStatisticsRepository getInstance() {
        return INSTANCE;
    }

    public AtomicInteger getAirportRequestCount(AirportData airportData) {
        return requestFrequency.computeIfAbsent(airportData, key -> new AtomicInteger());
    }

    public AtomicInteger getRadiusRequestCount(double radius) {
        return radiusFrequency.computeIfAbsent(radius, key -> new AtomicInteger());
    }

    public void incrementRequestFrequency(AirportData airportData) {
        getAirportRequestCount(airportData).incrementAndGet();
    }

    public void incrementRadiusFrequency(Double radius) {
        getRadiusRequestCount(radius).incrementAndGet();
    }

    public void clear() {
        requestFrequency.clear();
        radiusFrequency.clear();
    }
}
