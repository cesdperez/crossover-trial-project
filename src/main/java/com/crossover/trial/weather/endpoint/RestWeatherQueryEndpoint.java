package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.crossover.trial.weather.service.StatisticsService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    private final static Logger LOG = LoggerFactory.getLogger(RestWeatherQueryEndpoint.class);

    /**
     * shared gson json to object factory
     */
    public static final Gson gson = new Gson();

    private StatisticsService statisticsService = new StatisticsService();
    private AirportWeatherService airportWeatherService = new AirportWeatherService();

    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();

        retval.put("datasize", statisticsService.calculateDatasize());
        retval.put("iata_freq", statisticsService.calculateIataFrequency());
        retval.put("radius_freq", statisticsService.calculateRadiusFrequencyHistogram());

        return gson.toJson(retval);
    }

    @Override
    public Response weather(String iata, String radiusString) {
        try {
            double radius = radiusString == null ? 0 : Double.valueOf(radiusString);
            statisticsService.updateRequestFrequency(iata, radius);
            List<AtmosphericInformation> retval = airportWeatherService.findAtmosphericInformationInRange(iata, radius);
            LOG.debug("Atmospheric information for iata {} in a radius {} retrieved", iata, radius);

            return Response.status(Response.Status.OK)
                    .entity(retval)
                    .build();

        } catch (NoSuchElementException e) {
            LOG.debug("Atmospheric information couldn't be found. Airport ({}) doesn't exist doesn't exist.", iata);
            return Response.status(NOT_FOUND).build();
        }
    }
}
