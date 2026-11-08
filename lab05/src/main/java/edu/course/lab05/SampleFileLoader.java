package edu.course.lab05;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SampleFileLoader {

    private static final String HEADER = "id;label;source;featureCount;status";
    private static final String[] FIELD_NAMES = {"id", "label", "source", "featureCount", "status"};

    public LoadResult load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        List<String> lines = readLines(path);

        List<DataSample> samples = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        if (lines.isEmpty()) {
            errors.add(new ValidationError(1, "header", "header is missing"));
            return new LoadResult(samples, errors);
        }
        if (!HEADER.equals(removeBom(lines.get(0)).trim())) {
            errors.add(new ValidationError(1, "header", "expected header " + HEADER));
            return new LoadResult(samples, errors);
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            parseLine(i + 1, line, samples, errors);
        }
        return new LoadResult(samples, errors);
    }

    private static List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadingException("Cannot read " + path, e);
        }
    }

    private static String removeBom(String text) {
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
            return text.substring(1);
        }
        return text;
    }

    private static void parseLine(int lineNumber, String line,
                                  List<DataSample> samples, List<ValidationError> errors) {
        String[] parts;
        try {
            parts = FieldSplitter.split(line);
        } catch (IllegalArgumentException e) {
            errors.add(new ValidationError(lineNumber, "line", e.getMessage()));
            return;
        }
        if (parts.length != FIELD_NAMES.length) {
            errors.add(new ValidationError(lineNumber, "columns",
                    "expected " + FIELD_NAMES.length + " fields but found " + parts.length));
            return;
        }

        String[] fields = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            fields[i] = parts[i].trim();
            if (fields[i].isEmpty()) {
                errors.add(new ValidationError(lineNumber, FIELD_NAMES[i], "must not be empty"));
                return;
            }
        }

        int featureCount;
        try {
            featureCount = Integer.parseInt(fields[3]);
        } catch (NumberFormatException e) {
            errors.add(new ValidationError(lineNumber, "featureCount", "must be an integer"));
            return;
        }
        if (featureCount <= 0) {
            errors.add(new ValidationError(lineNumber, "featureCount", "must be positive"));
            return;
        }

        SampleStatus status;
        try {
            status = SampleStatus.valueOf(fields[4]);
        } catch (IllegalArgumentException e) {
            errors.add(new ValidationError(lineNumber, "status", "unknown status " + fields[4]));
            return;
        }

        samples.add(new DataSample(fields[0], fields[1], fields[2], featureCount, status));
    }
}