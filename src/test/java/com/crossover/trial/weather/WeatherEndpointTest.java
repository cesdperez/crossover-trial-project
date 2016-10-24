package com.crossover.trial.weather;

import com.crossover.trial.weather.endpoint.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.endpoint.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.endpoint.WeatherCollectorEndpoint;
import com.crossover.trial.weather.endpoint.WeatherQueryEndpoint;
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

public class WeatherEndpointTest {

    private WeatherQueryEndpoint query = new RestWeatherQueryEndpoint();

    private WeatherCollectorEndpoint update = new RestWeatherCollectorEndpoint();

    private Gson gson = new Gson();

    private DataPoint dataPoint;

    @Before
    public void setUp() throws Exception {
        InMemoryStatisticsRepository.getInstance().clear();
        InMemoryAirportWeatherRepository.getInstance().clear();

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
        update.updateWeather("JFK", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(40);
        update.updateWeather("EWR", "wind", gson.toJson(dataPoint));
        dataPoint.setMean(30);
        update.updateWeather("LGA", "wind", gson.toJson(dataPoint));

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

        update.updateWeather("BOS", "wind", gson.toJson(windDp));
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

        update.updateWeather("BOS", "cloudcover", gson.toJson(cloudCoverDp));

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