package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.repository.InMemoryAirportWeatherRepository;
import com.crossover.trial.weather.repository.InMemoryStatisticsRepository;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WeatherCollectorEndpointTest {

    public static final String IATA = "ABC";
    public static final String LATITUDE = "11";
    public static final String LONGITUDE = "22";

    private WeatherCollectorEndpoint collect;

    @Before
    public void setup() {
        InMemoryStatisticsRepository.getInstance().clear();
        InMemoryAirportWeatherRepository.getInstance().clear();
        collect = new RestWeatherCollectorEndpoint();
        collect.addAirport(IATA, LATITUDE, LONGITUDE);
    }

    @Test
    public void addAirportTest() {
        Response response = collect.addAirport("ZZZ", "1", "2");
        assertThat(fromStatusCode(response.getStatus()), is(CREATED));
    }

    @Test
    public void getAirportTest() {
        Response response = collect.getAirport("ABC");
        assertThat(fromStatusCode(response.getStatus()), is(OK));

        AirportData airport = (AirportData) response.getEntity();
        assertThat(airport.getIata(), is("ABC"));
        assertThat(airport.getLatitude(), is(Double.valueOf(LATITUDE)));
        assertThat(airport.getLongitude(), is(Double.valueOf(LONGITUDE)));
    }

    @Test
    public void getAirportsTest() {
        Response response = collect.getAirports();
        assertThat(fromStatusCode(response.getStatus()), is(OK));

        Set<String> airports = (Set<String>) response.getEntity();
        assertTrue(airports.contains("ABC"));
    }

    @Test
    public void deleteAirportTest() {
        Response response = collect.deleteAirport("ABC");
        assertThat(fromStatusCode(response.getStatus()), is(NO_CONTENT));

        response = collect.getAirports();
        assertThat(fromStatusCode(response.getStatus()), is(OK));

        Set<String> airports = (Set<String>) response.getEntity();
        assertFalse(airports.contains("ABC"));
    }
}
