package com.crossover.trial.weather.service;

import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.model.DataPointType;
import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class AirportWeatherService {

    private InMemoryAirportWeatherRepository airportRepository = InMemoryAirportWeatherRepository.getInstance();

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public AirportData findAirportDataFor(String iataCode) throws NoSuchElementException {
        return airportRepository.getAirports().stream()
                .filter(ap -> ap.getIata().equals(iataCode))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public AtmosphericInformation findAtmosphericInformationFor(String iataCode) throws NoSuchElementException {
        final AirportData airport = findAirportDataFor(iataCode);
        return airportRepository.getAtmosphericInformationFor(airport);
    }

    public Collection<AtmosphericInformation> findAtmosphericInformationInRange(String iata, double radius) throws NoSuchElementException {
        return findAirportDataInRange(iata, radius).stream()
                .map(airport -> airportRepository.getAtmosphericInformationFor(airport))
                .filter(AtmosphericInformation::isNotEmpty)
                .collect(toList());
    }

    private List<AirportData> findAirportDataInRange(String iata, double radius) throws NoSuchElementException {
        AirportData a1 = findAirportDataFor(iata);

        return airportRepository.getAirports().stream()
                .filter(a2 -> GeoService.getInstance().areInRange(a1, a2, radius))
                .collect(toList());
    }

    public Set<String> getAirportsIatas() {
        return airportRepository.getAirports().stream()
                .map(airportData -> airportData.getIata())
                .collect(toSet());
    }

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode  the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp        a datapoint object holding pointType data
     * @throws WeatherException if the update can not be completed
     */
    public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
        AtmosphericInformation atmosphericInformation = findAtmosphericInformationFor(iataCode);
        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());
        atmosphericInformation.update(dptype, dp);
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode  3 letter code
     * @param latitude  in degrees
     * @param longitude in degrees
     * @return the added airport
     */
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        return airportRepository.addAirport(iataCode, latitude, longitude);
    }
}
