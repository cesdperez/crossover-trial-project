package com.crossover.trial.weather.service;

import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;
import com.crossover.trial.weather.repository.InMemoryStatisticsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsService {

    public static final double RADIUS_HISTOGRAM_MAXSIZE = 1000.0;
    public static final int RADIUS_HISTOGRAM_BINSIZE = 10;

    private InMemoryAirportWeatherRepository airportRepository = InMemoryAirportWeatherRepository.getInstance();
    private InMemoryStatisticsRepository statisticsRepository = InMemoryStatisticsRepository.getInstance();

    private AirportWeatherService airportWeatherService = new AirportWeatherService();

    public int calculateDatasize() {
        return (int) airportRepository.getAtmosphericInformations().stream()
                .filter(AtmosphericInformation::isNotEmpty)
                .filter(AtmosphericInformation::wasUpdatedToday)
                .count();
    }

    public Map<String, Double> calculateIataFrequency() {
        Map<String, Double> freq = new HashMap<>();

        airportRepository.getAirports().forEach(airport -> {
            String iata = airport.getIata();
            double airportRequestFrequency = getAirportRequestFrequency(airport);
            freq.put(iata, airportRequestFrequency);
        });

        return freq;
    }

    private double getAirportRequestFrequency(AirportData airportData) {
        int airportRequestCount = statisticsRepository.getAirportRequestCount(airportData).get();
        int totalRequests = statisticsRepository.getRequestFrequency().size();

        if (totalRequests == 0) return 0;

        return (double) airportRequestCount / totalRequests;
    }

    public int[] calculateRadiusFrequencyHistogram() {
        Map<Double, AtomicInteger> radiusFrequency = statisticsRepository.getRadiusFrequency();

        int binCount = calculateHistogramBinCount(radiusFrequency);
        int[] hist = populateFrequencyHistogram(radiusFrequency, binCount);

        return hist;
    }

    private int[] populateFrequencyHistogram(Map<Double, AtomicInteger> radiusFrequency, int binCount) {
        int[] histogram = new int[binCount];

        radiusFrequency.entrySet().forEach(entry -> {
            int i = entry.getKey().intValue() / RADIUS_HISTOGRAM_BINSIZE;
            histogram[i] += entry.getValue().get();
        });

        return histogram;
    }

    private int calculateHistogramBinCount(Map<Double, AtomicInteger> radiusFrequency) {
        int maxSize = radiusFrequency.keySet().stream()
                .max(Double::compare).orElse(RADIUS_HISTOGRAM_MAXSIZE).intValue();
        return maxSize / RADIUS_HISTOGRAM_BINSIZE + 1;
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata   an iata code
     * @param radius query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = airportWeatherService.findAirportDataFor(iata);
        statisticsRepository.incrementRequestFrequency(airportData);
        statisticsRepository.incrementRadiusFrequency(radius);
    }
}
