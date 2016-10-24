package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    public final static Logger LOG = LoggerFactory.getLogger(RestWeatherCollectorEndpoint.class);

    /**
     * shared gson json to object factory
     */
    public final static Gson gson = new Gson();

    private AirportWeatherService airportWeatherService = new AirportWeatherService();

    @Override
    public Response ping() {
        return Response
                .status(OK)
                .entity("ready")
                .build();
    }

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
        try {
            airportWeatherService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
            LOG.debug("Data point ({} {} {}) added", iataCode, pointType, datapointJson);
            return Response.status(OK).build();
        } catch (WeatherException e) {
            LOG.error("Data point ({} {} {}) can't be added", iataCode, pointType, datapointJson);
            return Response.status(BAD_REQUEST).build();
        }
    }


    @Override
    public Response getAirports() {
        return Response
                .status(OK)
                .entity(airportWeatherService.getAirportsIatas())
                .build();
    }


    @Override
    public Response getAirport(String iata) {
        return Response
                .status(OK)
                .entity(airportWeatherService.findAirportDataFor(iata))
                .build();
    }


    @Override
    public Response addAirport(String iata, String latString, String longString) {
        try {
            airportWeatherService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
            LOG.debug("Airport (%s, %s, %s) added", iata, latString, longString);
            return Response.status(CREATED).build();
        } catch (NumberFormatException e) {
            LOG.error("Airport (%s, %s, %s) can't be added", iata, latString, longString, e);
            return Response.status(BAD_REQUEST).build();
        } catch (IllegalArgumentException e) {
            LOG.debug("Airport (%s, %s, %s) wasn't added, already exists", iata, latString, longString);
            return Response.status(NOT_MODIFIED).build();
        }
    }


    @Override
    public Response deleteAirport(String iata) {
        LOG.debug("deleteAirport ({})", iata);

        airportWeatherService.removeAirport(iata);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response
                .noContent()
                .build();
    }
}
