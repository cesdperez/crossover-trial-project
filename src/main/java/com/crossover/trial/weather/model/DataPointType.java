package com.crossover.trial.weather.model;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType {
    WIND(0, Double.MAX_VALUE),
    TEMPERATURE(-50, 100),
    HUMIDITY(0, 100),
    PRESSURE(650, 800),
    CLOUDCOVER(0, 100),
    PRECIPITATION(0, 100);

    /**
     * the minimum mean value for this data point type to be valid.
     */
    private double minMean;

    /**
     * the maximum mean value for this data point type to be valid.
     */
    private double maxMean;

    /**
     * @param minMean the minimum mean value for this data point type to be valid.
     * @param maxMean the maximum mean value for this data point type to be valid.
     */
    DataPointType(double minMean, double maxMean) {
        this.minMean = minMean;
        this.maxMean = maxMean;
    }

    public double getMinMean() {
        return minMean;
    }

    public double getMaxMean() {
        return maxMean;
    }
}
