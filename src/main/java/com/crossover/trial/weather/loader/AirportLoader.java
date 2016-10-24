package com.crossover.trial.weather.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.fromStatusCode;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * @author code test administrator
 */
public final class AirportLoader {

    private static final Logger LOG = LoggerFactory.getLogger(AirportLoader.class);

    public static final String TARGET_URL = "http://localhost:9090/collect";
    private static final String SERVICE_URL = "/airport/%s/%s/%s";

    public static final String DEFAULT_FILE = "target/classes/airports.dat";

    private final CsvReader csvReader;
    private final WebTarget collectorEndpoint;

    AirportLoader(CsvReader csvReader, WebTarget target) {
        this.csvReader = csvReader;
        this.collectorEndpoint = target;
    }

    public static void main(String args[]) throws IOException {
        if (!validateArgs(args))
            return;

        WebTarget client = ClientBuilder.newClient().target(TARGET_URL);
        CsvReader airportLineProvider = new CsvReader(new FileInputStream(args[0]));
        new AirportLoader(airportLineProvider, client).upload();
    }

    static boolean validateArgs(String[] args) {
        if (args.length == 0) {
            LOG.info("No input file has been provided, {} will be used", DEFAULT_FILE);
            args = new String[]{DEFAULT_FILE};
        }

        if (args.length != 1) {
            LOG.error("The airports.dat file path should be passed as argument");
            return false;
        }

        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0 || airportDataFile.isDirectory()) {
            LOG.error(airportDataFile + " is not a valid input");
            return false;
        }

        return true;
    }

    public void upload() {
        for (AirportLine line : csvReader.getAirportLines()) {
            try {
                Status responseStatus = addAirport(line);
                if (CREATED == responseStatus) {
                    LOG.info("Added " + line.iataCode);
                } else {
                    LOG.error("Failed to add airport " + line.iataCode + ", status is " + responseStatus);
                }
            } catch (ProcessingException e) {
                LOG.error("Failed when attempting to insert airport for " + line.iataCode, e);
            }
        }
    }

    private Status addAirport(AirportLine line) throws ProcessingException {
        WebTarget query = collectorEndpoint.path(String.format(SERVICE_URL, line.iataCode, line.latitude, line.longitude));
        Response response = query.request().post(null);
        return fromStatusCode(response.getStatus());
    }

}
