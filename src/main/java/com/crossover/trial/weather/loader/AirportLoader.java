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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * @author code test administrator
 */
public class AirportLoader {

    private static final Logger LOG = LoggerFactory.getLogger(AirportLoader.class);

    private static final String TARGET_URL = "http://localhost:9090/collect";
    private static final String SERVICE_URL = "/airport/%s/%s/%s";

    private static final String DEFAULT_FILE = "target/classes/airports.dat";

    private final CsvReader csvReader;
    private final WebTarget collectorEndpoint;

    AirportLoader(CsvReader csvReader, WebTarget target) {
        this.csvReader = csvReader;
        this.collectorEndpoint = target;
    }

    public static void main(String args[]) throws IOException {
        FileInputStream inputFile = parseArgs(args);

        if (inputFile == null) return;

        WebTarget client = ClientBuilder.newClient().target(TARGET_URL);

        CsvReader airportLineProvider = new CsvReader(inputFile);

        new AirportLoader(airportLineProvider, client).upload();
    }

    static FileInputStream parseArgs(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            LOG.info("No input file has been provided, {} will be used", DEFAULT_FILE);
            return new FileInputStream(DEFAULT_FILE);
        }

        if (args.length != 1) {
            LOG.error("Just the airports.dat file path should be passed as argument");
            return null;
        }

        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0 || airportDataFile.isDirectory()) {
            LOG.error("{} is not a valid input", airportDataFile);
            return new FileInputStream(airportDataFile);
        }

        return new FileInputStream(airportDataFile);
    }

    public void upload() {
        List<CsvFileLine> airportLines = csvReader.getAirportLines();
        for (int i = 0; i < airportLines.size(); i++) {
            CsvFileLine line = airportLines.get(i);

            if (line == null) {
                LOG.error("Failed parsing line number {}", i + 1);
                continue;
            }

            Status responseStatus = addAirport(line);

            if (CREATED == responseStatus)
                LOG.info("{} added", line);
            else if (BAD_REQUEST == responseStatus)
                LOG.warn("{} wasn't added, bad request", line);
            else if (NOT_MODIFIED == responseStatus)
                LOG.warn("{} wasn't added, already exists", line);
            else
                LOG.warn("{} wasn't added, unknown reason", line);
        }
    }

    private Status addAirport(CsvFileLine line) throws ProcessingException {
        WebTarget query = collectorEndpoint.path(String.format(SERVICE_URL, line.iataCode, line.latitude, line.longitude));
        Response response = query.request().post(null);
        return fromStatusCode(response.getStatus());
    }

}
