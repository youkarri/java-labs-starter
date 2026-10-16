package edu.course.lab02;

import java.util.Arrays;

/**
 * Наблюдение набора данных для обучения модели.
 *
 * Инварианты:
 * - id и label не null и не пустые;
 * - status не null;
 * - features не null, не пуст и содержит только конечные числа;
 * - внутренний массив признаков недоступен снаружи: он копируется
 *   при создании и при выдаче.
 */
public final class DataSample {

    private final String id;
    private final String label;
    private SampleStatus status;
    private final double[] features;

    public DataSample(String id, String label, SampleStatus status, double[] features) {
        requireText(id, "id");
        requireText(label, "label");
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        if (features == null || features.length == 0) {
            throw new IllegalArgumentException("features must not be null or empty");
        }
        for (double value : features) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("features must be finite numbers");
            }
        }
        this.id = id;
        this.label = label;
        this.status = status;
        this.features = Arrays.copyOf(features, features.length);
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

    public SampleStatus getStatus() {
        return status;
    }

    /** Возвращает копию признаков, чтобы внутреннее состояние нельзя было изменить снаружи. */
    public double[] getFeatures() {
        return Arrays.copyOf(features, features.length);
    }

    /** Единственный способ изменить статус. */
    public void changeStatus(SampleStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        this.status = newStatus;
    }

    /** Наблюдение готово, если его статус READY. */
    public boolean isReady() {
        return status == SampleStatus.READY;
    }

    /** Среднее значение признаков. */
    public double averageFeature() {
        double sum = 0;
        for (double value : features) {
            sum += value;
        }
        return sum / features.length;
    }

    /**
     * Возвращает НОВОЕ наблюдение с признаками в диапазоне [0, 1]
     * (min-max нормализация). Исходный объект не меняется.
     * Если все признаки равны, результат состоит из нулей.
     */
    public DataSample normalized() {
        double min = features[0];
        double max = features[0];
        for (double value : features) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        double range = max - min;
        double[] result = new double[features.length];
        if (range != 0) {
            for (int i = 0; i < features.length; i++) {
                result[i] = (features[i] - min) / range;
            }
        }
        return new DataSample(id, label, status, result);
    }
}