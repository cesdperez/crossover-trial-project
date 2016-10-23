package com.crossover.trial.weather;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WeatherEndpointTest {

    private WeatherQueryEndpoint query = new RestWeatherQueryEndpoint();

    private WeatherCollectorEndpoint update = new RestWeatherCollectorEndpoint();

    private Gson gson = new Gson();

    private DataPoint dataPoint;

    @Before
    public void setUp() throws Exception {
        RestWeatherQueryEndpoint.init();
        dataPoint = new DataPoint.Builder()
                .withCount(10)
                .withFirst(10)
                .withSecond(20)
                .withThird(30)
                .withMean(22)
                .build();
        update.updateWeather("BOS", "wind", gson.toJson(dataPoint));
        query.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), dataPoint);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        update.updateWeather("JFK", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(40);
        update.updateWeather("EWR", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(30);
        update.updateWeather("LGA", "wind", gson.toJson(dataPoint));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder()
                .withCount(10).withFirst(10).withSecond(20).withThird(30).withMean(22).build();
        update.updateWeather("BOS", "wind", gson.toJson(windDp));
        query.weather("BOS", "0").getEntity();

        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint.Builder()
                .withCount(4).withFirst(10).withSecond(60).withThird(100).withMean(50).build();
        update.updateWeather("BOS", "cloudcover", gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

}