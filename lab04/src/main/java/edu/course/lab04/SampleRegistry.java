package edu.course.lab04;

import java.util.HashMap;
import java.util.Map;

public final class SampleRegistry {

    private final Map<SampleId, DataSample> samples = new HashMap<>();

    public void put(SampleId id, DataSample sample) {
        if (id == null || sample == null) {
            throw new IllegalArgumentException("id and sample must not be null");
        }
        samples.put(id, sample);
    }

    public DataSample get(SampleId id) {
        return samples.get(id);
    }

    public DataSample remove(SampleId id) {
        return samples.remove(id);
    }

    public boolean containsKey(SampleId id) {
        return samples.containsKey(id);
    }

    public int size() {
        return samples.size();
    }
}