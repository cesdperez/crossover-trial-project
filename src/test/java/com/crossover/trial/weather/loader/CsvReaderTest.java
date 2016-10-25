package com.crossover.trial.weather.loader;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CsvReaderTest {

    private static final String TEST_FILE = "src/test/resources/airports.dat";

    CsvReader csvReader;

    @Before
    public void setup() throws Exception {
        FileInputStream inputFile = new FileInputStream(TEST_FILE);
        csvReader = new CsvReader(inputFile);
    }

    @Test
    public void lineCountTest() {
        List<CsvFileLine> lines = csvReader.getLines();
        assertThat(lines.size(), is(14));
    }

    @Test
    public void normalCaseTest() {
        List<CsvFileLine> lines = csvReader.getLines();
        assertEquals(lines.get(1), new CsvFileLine("EWR", "-74.168667", "40.6925"));
    }

    @Test
    public void noQuotesTest() {
        List<CsvFileLine> lines = csvReader.getLines();
        assertEquals(lines.get(6), new CsvFileLine("LHR", "-0.461389", "51.4775"));
    }

    @Test
    public void spacesTest() {
        List<CsvFileLine> lines = csvReader.getLines();
        assertEquals(lines.get(7), new CsvFileLine("LTN", " -0.368333", "51.874722"));
    }

    @Test
    public void malformedLinesTest() {
        List<CsvFileLine> lines = csvReader.getLines();
        assertNull(lines.get(4));
        assertNull(lines.get(10));
        assertNull(lines.get(12));
        assertNull(lines.get(13));
    }
}
