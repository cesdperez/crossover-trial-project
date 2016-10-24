package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

    /**
     * shared gson json to object factory
     */
    public final static Gson gson = new Gson();

    private AirportWeatherService airportWeatherService = new AirportWeatherService();

    @Override
    public Response ping() {
        return Response
                .status(Response.Status.OK)
                .entity("ready")
                .build();
    }

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
        try {
            airportWeatherService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            e.printStackTrace();
        }
        return Response
                .status(Response.Status.OK)
                .build();
    }


    @Override
    public Response getAirports() {
        return Response
                .status(Response.Status.OK)
                .entity(airportWeatherService.getAirportsIatas())
                .build();
    }


    @Override
    public Response getAirport(String iata) {
        return Response
                .status(Response.Status.OK)
                .entity(airportWeatherService.findAirportDataFor(iata))
                .build();
    }


    @Override
    public Response addAirport(String iata, String latString, String longString) {
        airportWeatherService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response
                .status(Response.Status.OK)
                .build();
    }


    @Override
    public Response deleteAirport(String iata) {
        return Response
                .status(Response.Status.NOT_IMPLEMENTED)
                .build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response
                .noContent()
                .build();
    }
}
