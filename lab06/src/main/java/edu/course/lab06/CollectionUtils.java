package edu.course.lab06;

import java.util.Collection;

/**
 * Дополнительная часть. Копирование между коллекциями с учетом правила PECS
 * (Producer Extends, Consumer Super).
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Утилитарный класс не должен иметь экземпляров.
    }

    /**
     * Добавляет все элементы source в target.
     * source только отдает элементы (producer), поэтому ? extends T.
     * target только принимает элементы (consumer), поэтому ? super T.
     */
    public static <T> void copyAll(Collection<? extends T> source, Collection<? super T> target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source and target must not be null");
        }
        for (T item : source) {
            target.add(item);
        }
    }
}
