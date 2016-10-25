package com.crossover.trial.weather.repository;

import com.crossover.trial.weather.exception.AirportAlreadyExistsException;
import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.model.AtmosphericInformation;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Airport Weather Domain data that supports concurrency.
 */
public class InMemoryAirportWeatherRepository {
    /**
     * Initial capacity to support
     */
    private static final int DEFAULT_CAPACITY = 1000;

    /**
     * airports and their atmospheric information, key corresponds with airportData
     */
    private final Map<AirportData, AtmosphericInformation> airportMap;

    /**
     * Singleton instance
     */
    private static final InMemoryAirportWeatherRepository INSTANCE = new InMemoryAirportWeatherRepository();

    /**
     * private constructor to prevent malicious instantiations.
     */
    private InMemoryAirportWeatherRepository() {
        airportMap = new ConcurrentHashMap<>(DEFAULT_CAPACITY);
        init();
    }

    /**
     * Gets the singleton instance of this class
     *
     * @return {@link InMemoryAirportWeatherRepository} instance
     */
    public static InMemoryAirportWeatherRepository getInstance() {
        return INSTANCE;
    }

    public Collection<AirportData> getAirports() {
        return airportMap.keySet();
    }

    public Collection<AtmosphericInformation> getAtmosphericInformations() {
        return airportMap.values();
    }

    public AtmosphericInformation getAtmosphericInformationFor(AirportData airport) {
        return airportMap.get(airport);
    }

    public void addAirport(AirportData ad, AtmosphericInformation ai) {
        AtmosphericInformation atmosphericInformation = airportMap.putIfAbsent(ad, ai);
        if (null != atmosphericInformation)
            throw new AirportAlreadyExistsException();
    }

    public void addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData.Builder()
                .withIata(iataCode)
                .withLatitude(latitude)
                .withLongitude(longitude)
                .build();

        AtmosphericInformation ai = new AtmosphericInformation();

        addAirport(ad, ai);
    }

    public void removeAirport(AirportData airport) {
        airportMap.remove(airport);
    }

    /**
     * A dummy init method that loads hard coded data
     */

    public void init() {
        addAirport("BOS", 42.364347, -71.005181);
        addAirport("EWR", 40.6925, -74.168667);
        addAirport("JFK", 40.639751, -73.778925);
        addAirport("LGA", 40.777245, -73.872608);
        addAirport("MMU", 40.79935, -74.4148747);
    }

    public void clear() {
        airportMap.clear();
        init();
    }
}
