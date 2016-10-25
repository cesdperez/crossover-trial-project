package com.crossover.trial.weather.loader;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A simple CSV Reader
 */
class CsvReader {

    private static final int IATA = 4;
    private static final int LATITUDE = 6;
    private static final int LONGITUDE = 7;

    private final List<CsvFileLine> airportLines;

    CsvReader(InputStream csvStream) throws IOException {
        try (CSVReader strings = new CSVReader(new InputStreamReader(csvStream))) {
            airportLines = strings.readAll()
                    .stream()
                    .map(row -> getAirportLine(row))
                    .collect(toList());
        }
    }

    private CsvFileLine getAirportLine(String[] row) {
        try {
            return new CsvFileLine(row[IATA], row[LONGITUDE], row[LATITUDE]);
        } catch (Exception e) {
            return null;
        }
    }

    List<CsvFileLine> getAirportLines() {
        return new ArrayList<>(airportLines);
    }

}
