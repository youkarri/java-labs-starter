package edu.course.lab01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CourseToolkitTest {

    @Test
    void returnsTrueForEvenNumber() {
        boolean result = CourseToolkit.isEven(8);

        assertTrue(result);
    }

    @Test
    void returnsFalseForOddNumber() {
        boolean result = CourseToolkit.isEven(7);

        assertFalse(result);
    }

    @Test
    void zeroIsEven() {
        assertTrue(CourseToolkit.isEven(0));
    }

    @Test
    void returnsTrueForNegativeEvenNumber() {
        assertTrue(CourseToolkit.isEven(-8));
    }

    @Test
    void returnsFalseForNumbersLessThanTwo() {
        assertFalse(CourseToolkit.isPrime(1));
        assertFalse(CourseToolkit.isPrime(0));
        assertFalse(CourseToolkit.isPrime(-7));
    }

    @Test
    void returnsTrueForTwo() {
        assertTrue(CourseToolkit.isPrime(2));
    }

    @Test
    void returnsFalseForCompositeNumber() {
        assertFalse(CourseToolkit.isPrime(9));
    }

    @Test
    void returnsFalseForSquareOfPrime() {
        assertFalse(CourseToolkit.isPrime(49));
    }

    @Test
    void returnsTrueForPrimeNumber() {
        assertTrue(CourseToolkit.isPrime(17));
    }

    @Test
    void returnsTrueForPalindrome() {
        assertTrue(CourseToolkit.isPalindrome("level"));
    }

    @Test
    void returnsFalseWhenCaseDiffers() {
        assertFalse(CourseToolkit.isPalindrome("Level"));
    }

    @Test
    void returnsFalseForNonPalindrome() {
        assertFalse(CourseToolkit.isPalindrome("hello"));
    }

    @Test
    void throwsForNullText() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.isPalindrome(null));
    }

    @Test
    void calculatesAverageOfPositiveNumbers() {
        assertEquals(2.5, CourseToolkit.average(new int[]{1, 2, 3, 4}), 0.0001);
    }

    @Test
    void calculatesAverageWithNegativeNumbers() {
        assertEquals(-2.0, CourseToolkit.average(new int[]{-1, -2, -3}), 0.0001);
    }

    @Test
    void throwsForNullArray() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(null));
    }

    @Test
    void throwsForEmptyArray() {
        assertThrows(IllegalArgumentException.class,
                () -> CourseToolkit.average(new int[]{}));
    }

    @Test
    void doesNotModifyInputArray() {
        int[] values = {1, 2, 3};
        CourseToolkit.average(values);
        assertArrayEquals(new int[]{1, 2, 3}, values);
    }

    @Test
    void findsMinimum() {
        assertEquals(-5, CourseToolkit.min(new int[]{3, -5, 10, 0}));
    }

    @Test
    void findsMaximum() {
        assertEquals(10, CourseToolkit.max(new int[]{3, -5, 10, 0}));
    }

    @Test
    void minAndMaxWorkForSingleElement() {
        assertEquals(7, CourseToolkit.min(new int[]{7}));
        assertEquals(7, CourseToolkit.max(new int[]{7}));
    }

    @Test
    void minAndMaxThrowForNullArray() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.min(null));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.max(null));
    }

    @Test
    void minAndMaxThrowForEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.min(new int[]{}));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.max(new int[]{}));
    }

}
