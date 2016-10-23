package com.crossover.trial.weather.model;

import java.util.Arrays;

/**
 * Daylight saving time zones
 */
public enum DaylightSavingsTime {
    E("Europe"),
    A("US/Canada"),
    S("South America"),
    O("Australia"),
    Z("New Zealand"),
    N("None"),
    U("Unknown");

    private String name;

    DaylightSavingsTime(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Gets corresponding DaylightSavingsTime zone from given name.
     * The comparison is case insensitive, therefore "europe" and "EUROPE" will retrieve same enum.
     *
     * @param name
     * @return corresponding enum or Unknown (DaylightSavingsTime.U)
     */
    public DaylightSavingsTime fromName(String name) {
        return Arrays.stream(values())
                .filter(zone -> zone.name.equalsIgnoreCase(name))
                .findFirst().orElse(DaylightSavingsTime.U);
    }
}
