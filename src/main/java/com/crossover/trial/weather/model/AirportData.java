package com.crossover.trial.weather.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static com.crossover.trial.weather.exception.WeatherValidator.*;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /**
     * Name of the airport
     */
    private String name;

    /**
     * Main city served by airport. May be spelled differently from name.
     */
    private String city;

    /**
     * Country or territory where airport is located.
     */
    private String country;

    /**
     * the three letter IATA code
     */
    private String iata;

    /**
     * 4-letter ICAO code
     */
    private String icao;

    /**
     * airport location latitude
     */
    private double latitude;

    /**
     * airport location longitude
     */
    private double longitude;

    /**
     * altitude value in feets
     */
    private long altitude;

    /**
     * Hours offset from UTC.
     */
    private double timezone;

    /**
     * Day light saving time of airport
     */
    private DaylightSavingsTime zone = DaylightSavingsTime.U;

    /**
     * Private constructor to prevent unwanted instantiation
     * Use Builder instead.
     */
    private AirportData(Builder builder) {
        name = builder.name;
        city = builder.city;
        country = builder.country;
        iata = builder.iata;
        icao = builder.icao;
        latitude = builder.latitude;
        longitude = builder.longitude;
        altitude = builder.altitude;
        timezone = builder.timezone;
        zone = builder.zone;
    }

    public String getName() {
        return name;
    }

    public String getIata() {
        return iata;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIcao() {
        return icao;
    }

    public long getAltitude() {
        return altitude;
    }

    public double getTimezone() {
        return timezone;
    }

    public DaylightSavingsTime getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("city", city)
                .append("country", country)
                .append("iata", iata)
                .append("icao", icao)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("altitude", altitude)
                .append("timezone", timezone)
                .append("zone", zone)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportData that = (AirportData) o;
        return Objects.equals(iata, that.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata);
    }

    /**
     * Builder to create airport data instances.
     *
     * @author emiralp
     */
    public static class Builder {
        private String name;
        private String city;
        private String country;
        private String iata;
        private String icao;
        private double latitude;
        private double longitude;
        private long altitude;
        private double timezone;
        private DaylightSavingsTime zone;

        public Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder withIata(String iata) throws IllegalArgumentException {
            checkIata(iata);
            this.iata = iata.trim();
            return this;
        }

        public Builder withIcao(String icao) throws IllegalArgumentException {
            checkIcao(icao);
            this.icao = icao.trim();
            return this;
        }

        public Builder withLatitude(double latitude) throws IllegalArgumentException {
            checkLatitude(latitude);
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(double longitude) throws IllegalArgumentException {
            checkLongitude(longitude);
            this.longitude = longitude;
            return this;
        }

        public Builder withAltitude(long altitude) {
            this.altitude = altitude;
            return this;
        }

        public Builder withTimezone(double timezone) {
            this.timezone = timezone;
            return this;
        }

        public Builder withDSTZone(DaylightSavingsTime zone) {
            this.zone = zone;
            return this;
        }

        public AirportData build() {
            return new AirportData(this);
        }
    }
}
