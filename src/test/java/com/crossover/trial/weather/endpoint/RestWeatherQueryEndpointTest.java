package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;
import com.crossover.trial.weather.repository.InMemoryStatisticsRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RestWeatherQueryEndpointTest {

    private WeatherQueryEndpoint query;

    private WeatherCollectorEndpoint collect;

    private Gson gson;

    private DataPoint dataPoint;

    @Before
    public void setUp() throws Exception {
        InMemoryStatisticsRepository.getInstance().clear();
        InMemoryAirportWeatherRepository.getInstance().clear();

        query = new RestWeatherQueryEndpoint();
        collect = new RestWeatherCollectorEndpoint();
        gson = new Gson();

        dataPoint = new DataPoint.Builder()
                .withCount(10)
                .withFirst(10)
                .withSecond(20)
                .withThird(30)
                .withMean(22)
                .build();

        collect.updateWeather("BOS", "wind", gson.toJson(dataPoint));
        query.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertPing(pingResult, 1, 5, 1);
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), dataPoint);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        collect.updateWeather("JFK", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(40);
        collect.updateWeather("EWR", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(30);
        collect.updateWeather("LGA", "wind", gson.toJson(dataPoint));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("JFK", "30").getEntity();
        assertEquals(2, ais.size());

        ais = (List<AtmosphericInformation>) query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());


        ais = (List<AtmosphericInformation>) query.weather("JFK", "500").getEntity();
        assertEquals(4, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder()
                .withCount(10)
                .withFirst(10)
                .withSecond(20)
                .withThird(30)
                .withMean(22)
                .build();

        collect.updateWeather("BOS", "wind", gson.toJson(windDp));
        query.weather("BOS", "0").getEntity();

        String ping = query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertPing(pingResult, 1, 5, 2);

        DataPoint cloudCoverDp = new DataPoint.Builder()
                .withCount(4)
                .withFirst(10)
                .withSecond(60)
                .withThird(100)
                .withMean(50)
                .build();

        collect.updateWeather("BOS", "cloudcover", gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).getWind(), windDp);
        assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
    }

    private void assertPing(JsonElement pingResult, int datasize, int iataFreq, int radiusFreq) {
        assertEquals(datasize, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(iataFreq, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
        assertEquals(radiusFreq, pingResult.getAsJsonObject().get("radius_freq").getAsJsonArray().get(0).getAsInt());
    }

}