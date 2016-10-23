package com.crossover.trial.weather.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    private double mean = 0.0;

    private int first = 0;

    private int median = 0;

    private int last = 0;

    private int count = 0;

    /**
     * private constructor, use the builder to create this object
     */
    private DataPoint() {
    }

    private DataPoint(Builder builder) {
        this.setFirst(builder.first);
        this.setMean(builder.mean);
        this.setMedian(builder.median);
        this.setLast(builder.last);
        this.setCount(builder.count);
    }

    /**
     * the mean of the observations
     */
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * 1st quartile -- useful as a lower bound
     */
    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * 2nd quartile -- median value
     */
    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    /**
     * 3rd quartile value -- less noisy upper value
     */
    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    /**
     * the total number of measurements
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("mean", mean)
                .append("first", first)
                .append("median", median)
                .append("last", last)
                .append("count", count)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPoint dataPoint = (DataPoint) o;
        return Double.compare(dataPoint.mean, mean) == 0 &&
                first == dataPoint.first &&
                median == dataPoint.median &&
                last == dataPoint.last &&
                count == dataPoint.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean, first, median, last, count);
    }

    static public class Builder {
        private int first;
        private int mean;
        private int median;
        private int last;
        private int count;

        public Builder() {
        }

        public Builder withFirst(int first) {
            this.first = first;
            return this;
        }

        public Builder withMean(int mean) {
            this.mean = mean;
            return this;
        }

        public Builder withMedian(int median) {
            this.median = median;
            return this;
        }

        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder withLast(int last) {
            this.last = last;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}
