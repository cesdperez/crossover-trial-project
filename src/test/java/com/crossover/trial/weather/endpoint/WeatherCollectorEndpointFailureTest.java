package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;
import com.crossover.trial.weather.repository.InMemoryStatisticsRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WeatherCollectorEndpointFailureTest {

    private WeatherCollectorEndpoint collect;

    private Gson gson;

    @Before
    public void setup() {
        InMemoryStatisticsRepository.getInstance().clear();
        InMemoryAirportWeatherRepository.getInstance().clear();
        collect = new RestWeatherCollectorEndpoint();
        gson = new Gson();
    }

    @Test
    public void addAirportBadRequestTest() {
        Response response = collect.addAirport("ABC", "", "234");
        assertThat(fromStatusCode(response.getStatus()), is(BAD_REQUEST));

        response = collect.addAirport("ABC", "1", "2...4");
        assertThat(fromStatusCode(response.getStatus()), is(BAD_REQUEST));

        response = collect.addAirport("ABC", "91", "2");
        assertThat(fromStatusCode(response.getStatus()), is(BAD_REQUEST));
    }

    @Test
    public void addExistentAirportTest() {
        Response response = collect.addAirport("ABC", "80", "80");
        assertThat(fromStatusCode(response.getStatus()), is(CREATED));

        response = collect.addAirport("ABC", "8", "2");
        assertThat(fromStatusCode(response.getStatus()), is(NOT_MODIFIED));
    }

    @Test
    public void deleteNonexistentAirportTest() {
        Response response = collect.deleteAirport("ZZZ");
        assertThat(fromStatusCode(response.getStatus()), is(NOT_MODIFIED));
    }

    @Test
    public void getNonexistentAirportTest() {
        Response response = collect.getAirport("ZZZ");
        assertThat(fromStatusCode(response.getStatus()), is(NOT_FOUND));
    }

    @Test
    public void weatherUnexistentAirportTest() {
        Response response = collect.updateWeather("ZZZ", "wind", null);
        assertThat(fromStatusCode(response.getStatus()), is(NOT_FOUND));
    }

    @Test
    public void weatherBadRequestTest() {
        collect.addAirport("ZZZ", "1", "2");

        DataPoint windDp = buildADataPoint();
        String datapointJson = gson.toJson(windDp);

        Response response = collect.updateWeather("ZZZ", "temperature", datapointJson);
        assertThat(fromStatusCode(response.getStatus()), is(BAD_REQUEST));
    }

    private DataPoint buildADataPoint() {
        return new DataPoint.Builder()
                .withCount(10)
                .withFirst(10)
                .withSecond(20)
                .withThird(30)
                .withMean(1000)
                .build();
    }
}
