package com.crossover.trial.weather.endpoint;

import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.crossover.trial.weather.service.StatisticsService;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    public final static Logger LOGGER = Logger.getLogger("WeatherQuery");

    /**
     * shared gson json to object factory
     */
    public static final Gson gson = new Gson();

    private StatisticsService statisticsService = new StatisticsService();
    private AirportWeatherService airportWeatherService = new AirportWeatherService();

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();

        retval.put("datasize", statisticsService.calculateDatasize());
        retval.put("iata_freq", statisticsService.calculateIataFrequency());
        retval.put("radius_freq", statisticsService.calculateRadiusFrequencyHistogram());

        return gson.toJson(retval);
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata         the iataCode
     * @param radiusString the radius in km
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
        double radius = radiusString == null ? 0 : Double.valueOf(radiusString);
        statisticsService.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            AtmosphericInformation atmosphericInformation = airportWeatherService.findAtmosphericInformationFor(iata);
            retval.add(atmosphericInformation);
        } else {
            Collection<AtmosphericInformation> atmosphericInformationInRange = airportWeatherService.findAtmosphericInformationInRange(iata, radius);
            retval.addAll(atmosphericInformationInRange);
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }
}
