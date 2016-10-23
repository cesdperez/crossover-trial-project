package com.crossover.trial.weather.util;

import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.model.DataPointType;

import static org.apache.commons.lang3.Validate.*;

/**
 * Provides several methods for validating values of the Airport Weather Service domain.
 * <p>
 * If a validation doesn't pass, an {@link IllegalArgumentException} is thrown.
 */
public class WeatherValidator {

    private static final int IATA_CODE_LENGTH = 3;
    private static final int ICAO_CODE_LENGTH = 4;

    private static final double MINIMUM_LATITUDE = -90.0;
    private static final double MAXIMUM_LATITUDE = 90.0;

    private static final double MINIMUM_LONGITUDE = -180.0;
    private static final double MAXIMUM_LONGITUDE = 180.0;

    private static final int MAX_SIGNIFICANT_DIGITS = 6;

    public static void checkIcao(String icao) throws IllegalArgumentException {
        notBlank(icao);
        maximumTimedLength(icao, ICAO_CODE_LENGTH);
    }

    public static void checkIata(String iata) throws IllegalArgumentException {
        notBlank(iata);
        maximumTimedLength(iata, IATA_CODE_LENGTH);
    }

    private static void maximumTimedLength(String value, int length) {
        isTrue(value.trim().length() == length, "Value %s length must be less than %s", value, length);
    }

    public static void checkLatitude(double latitude) throws IllegalArgumentException {
        inclusiveBetween(MINIMUM_LATITUDE, MAXIMUM_LATITUDE, latitude);
    }

    public static void checkLongitude(double longitude) throws IllegalArgumentException {
        inclusiveBetween(MINIMUM_LONGITUDE, MAXIMUM_LONGITUDE, longitude);
    }

    public static void checkDataPoint(DataPointType dataPointType, DataPoint dataPoint) {
        isTrue(dataPoint.getMean() >= dataPointType.getMinMean());
        isTrue(dataPoint.getMean() < dataPointType.getMaxMean());
    }
}
