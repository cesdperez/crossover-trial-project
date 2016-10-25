package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.exception.AirportAlreadyExistsException;
import com.crossover.trial.weather.model.AirportData;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.NoSuchElementException;

import static javax.ws.rs.core.Response.Status.*;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    private final static Logger LOG = LoggerFactory.getLogger(RestWeatherCollectorEndpoint.class);

    /**
     * shared gson json to object factory
     */
    private final static Gson gson = new Gson();

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
            DataPoint dataPoint = gson.fromJson(datapointJson, DataPoint.class);
            airportWeatherService.addDataPoint(iataCode, pointType, dataPoint);
            LOG.debug("Data point ({} {} {}) added", iataCode, pointType, datapointJson);
            return Response.status(OK).build();
        } catch (IllegalArgumentException e) {
            LOG.debug("Data point ({} {} {}) can't be added", iataCode, pointType, datapointJson, e);
            return Response.status(BAD_REQUEST).build();
        } catch (NoSuchElementException e) {
            LOG.debug("Airport ({}) doesn't exist. Data point won't be added.", iataCode);
            return Response.status(NOT_FOUND).build();
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
        try {
            AirportData airport = airportWeatherService.findAirportDataFor(iata);
            LOG.debug("Data for airport ({}) retrieved", iata);
            return Response.status(OK)
                    .entity(airport)
                    .build();

        } catch (NoSuchElementException e) {
            LOG.debug("Airport ({}) can't be retrieved because it doesn't exist");
            return Response.status(NOT_FOUND).build();
        }
    }


    @Override
    public Response addAirport(String iata, String latString, String longString) {
        try {
            airportWeatherService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
            LOG.debug("Airport ({} {} {}) added", iata, latString, longString);
            return Response.status(CREATED).build();
        } catch (IllegalArgumentException e) {
            LOG.debug("Airport ({} {} {}) can't be added", iata, latString, longString, e);
            return Response.status(BAD_REQUEST).build();
        } catch (AirportAlreadyExistsException e) {
            LOG.debug("Airport ({} {} {}) wasn't added, already exists", iata, latString, longString);
            return Response.status(NOT_MODIFIED).build();
        }
    }


    @Override
    public Response deleteAirport(String iata) {
        try {
            airportWeatherService.removeAirport(iata);
            LOG.debug("Airport ({}) deleted", iata);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            LOG.debug("Airport ({}) doesn't exist", iata);
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response
                .noContent()
                .build();
    }
}
