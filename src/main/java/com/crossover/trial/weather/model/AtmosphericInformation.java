package com.crossover.trial.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.crossover.trial.weather.model.DataPointType.*;
import static com.crossover.trial.weather.util.WeatherValidator.checkDataPoint;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

    final Map<DataPointType, DataPoint> data = new ConcurrentHashMap<>();

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;

    public AtmosphericInformation() {
        lastUpdateTime = System.currentTimeMillis();
    }

    public DataPoint update(DataPointType key, DataPoint value) throws IllegalArgumentException {
        checkDataPoint(key, value);
        lastUpdateTime = System.currentTimeMillis();
        return data.put(key, value);
    }

    public DataPoint get(DataPointType key) {
        return data.get(key);
    }

    public DataPoint getTemperature() {
        return get(TEMPERATURE);
    }

    public DataPoint getWind() {
        return get(WIND);
    }

    public DataPoint getHumidity() {
        return get(HUMIDITY);
    }

    public DataPoint getPrecipitation() {
        return get(PRECIPITATION);
    }

    public DataPoint getPressure() {
        return get(PRESSURE);
    }

    public DataPoint getCloudCover() {
        return get(CLOUDCOVER);
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return data.isEmpty();
    }

}

