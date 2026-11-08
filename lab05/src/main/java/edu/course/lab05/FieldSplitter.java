package edu.course.lab05;

import java.util.ArrayList;
import java.util.List;

final class FieldSplitter {

    private static final char DELIMITER = ';';
    private static final char QUOTE = '"';

    private FieldSplitter() {
    }

    static String[] split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int length = line.length();
        int i = 0;
        while (true) {
            current.setLength(0);
            if (i < length && line.charAt(i) == QUOTE) {
                i++;
                boolean closed = false;
                while (i < length) {
                    char c = line.charAt(i);
                    if (c == QUOTE) {
                        if (i + 1 < length && line.charAt(i + 1) == QUOTE) {
                            current.append(QUOTE);
                            i += 2;
                        } else {
                            closed = true;
                            i++;
                            break;
                        }
                    } else {
                        current.append(c);
                        i++;
                    }
                }
                if (!closed) {
                    throw new IllegalArgumentException("unclosed quote");
                }
                if (i < length && line.charAt(i) != DELIMITER) {
                    throw new IllegalArgumentException("unexpected character after closing quote");
                }
            } else {
                while (i < length && line.charAt(i) != DELIMITER) {
                    current.append(line.charAt(i));
                    i++;
                }
            }
            fields.add(current.toString());
            if (i >= length) {
                break;
            }
            i++;
        }
        return fields.toArray(new String[0]);
    }
}