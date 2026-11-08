package edu.course.lab07;

/**
 * Наблюдение набора данных. Неизменяемый объект.
 */
public final class DataSample {

    private final String id;
    private final String label;
    private final String source;
    private final int featureCount;
    private final SampleStatus status;

    public DataSample(String id, String label, String source, int featureCount, SampleStatus status) {
        requireText(id, "id");
        requireText(label, "label");
        requireText(source, "source");
        if (featureCount <= 0) {
            throw new IllegalArgumentException("featureCount must be positive");
        }
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        this.id = id;
        this.label = label;
        this.source = source;
        this.featureCount = featureCount;
        this.status = status;
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be null or blank");
        }
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getSource() {
        return source;
    }

    public int getFeatureCount() {
        return featureCount;
    }

    public SampleStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "DataSample{id=" + id + ", label=" + label + ", source=" + source
                + ", featureCount=" + featureCount + ", status=" + status + "}";
    }
}
