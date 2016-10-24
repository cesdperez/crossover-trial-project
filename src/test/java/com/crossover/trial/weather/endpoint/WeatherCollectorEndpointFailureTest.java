package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;
import com.crossover.trial.weather.repository.InMemoryStatisticsRepository;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WeatherCollectorEndpointFailureTest {

    private WeatherCollectorEndpoint collect;

    @Before
    public void setup() {
        InMemoryStatisticsRepository.getInstance().clear();
        InMemoryAirportWeatherRepository.getInstance().clear();
        collect = new RestWeatherCollectorEndpoint();
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
}
