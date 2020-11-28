package test;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.QueriesNotStatic.*;

public class QueriesTest {

    /**
     * Uses expected.iterator::hasNext and asserts if actual.iterator::next is not null
     *
     * @param expected Expected iterable
     * @param actual   Actual iterable
     * @param <T>      type of Iterable
     */
    private static <T> void assertIterablesEqualNotNull(Iterable<T> expected, Iterable<T> actual) {
        for(Iterator<T> it = expected.iterator(), itres = actual.iterator(); it.hasNext(); it.next())
            assertNotNull(itres.next());
    }

    @Test
    public void testGenerator() {
        Iterable<Double> nrs = generate(Math::random).limit(7);
        of(nrs).forEach(System.out::println);
    }

    @Test
    public void testIterate() {
        // Arrange
        Iterable<Integer> expected = asList(1, 2, 3, 4, 5, 6, 7);

        // Act
        Iterable<Integer> actual = iterate(0, n -> ++n).takeWhile(n -> n < 8);

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }

    @Test
    public void testFilterWithNullElements() {
        // Arrange
        Iterable<String> strs = asList("ola", "super", null, "abc", null, "1234");
        Predicate<String> p = s -> s == null || s.length() == 3;
        Iterable<String> expected = asList("ola", null, "abc", null);

        // Act
        Iterable<String> actual = of(strs).filter(p);

        // Assert
        assertIterableEquals(expected, actual);
    }

    @Test
    public void testTakeWhileNotEmpty() {
        // Arrange
        Iterable<Integer> nrs = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 17, 31);
        Iterable<Integer> expected = asList(1, 2, 3, 4, 5, 6, 7);

        // Act
        Iterable<Integer> actual = of(nrs).takeWhile(n -> n < 8);

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }

    @Test
    public void testTakeWhileEmpty() {
        // Arrange
        Iterable<Integer> nrs = asList(1, 2, 3, 4, 5);
        Iterable<Integer> expected = Collections.emptyList();

        // Act
        Iterable<Integer> actual = of(nrs).takeWhile(n -> n < 0);

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }

    @Test
    public void testTakeWhileFull() {
        // Arrange
        Iterable<Integer> nrs = asList(1, 2, 3, 4, 5);

        // Act
        Iterable<Integer> actual = of(nrs).takeWhile(n -> n >= 0);

        // Assert
        assertIterablesEqualNotNull(nrs, actual);
    }

    @Test
    public void testFlatMapNotEmpty() {
        // Arrange
        Iterable<Integer> nrs = asList(2, 5, 8, 11);
        Iterable<Integer> expected = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        // Act
        Iterable<Integer> actual = of(nrs).flatMap(n -> asList(n - 1, n, n + 1));

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }

    @Test
    public void testFlatMapEmpty() {
        // Arrange
        Iterable<Integer> nrs = asList(2, 5, 8, 11);
        Iterable<Integer> expected = Collections.emptyList();

        // Act
        Iterable<Integer> actual = of(nrs).flatMap(n -> Collections.emptyList());

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }

    @Test
    public void testFlatMapOneEmpty() {
        // Arrange
        Iterable<Integer> nrs = asList(1, 2, 3);
        Iterable<Integer> expected = asList(0,1,2,1,2,3);

        // Act
        Iterable<Integer> actual = of(nrs).flatMap(n -> n < 3 ? asList(n - 1, n, n + 1) : Collections.emptyList());

        // Assert
        assertIterablesEqualNotNull(expected, actual);
    }
}