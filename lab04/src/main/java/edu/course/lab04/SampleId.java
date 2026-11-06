package edu.course.lab04;

import java.util.Objects;

public final class SampleId {

    private final String datasetName;
    private final int number;

    public SampleId(String datasetName, int number) {
        if (datasetName == null || datasetName.isBlank()) {
            throw new IllegalArgumentException("datasetName must not be null or blank");
        }
        if (number < 0) {
            throw new IllegalArgumentException("number must not be negative");
        }
        this.datasetName = datasetName;
        this.number = number;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        SampleId that = (SampleId) other;
        return number == that.number && datasetName.equals(that.datasetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetName, number);
    }

    @Override
    public String toString() {
        return "SampleId{datasetName=" + datasetName + ", number=" + number + "}";
    }
}