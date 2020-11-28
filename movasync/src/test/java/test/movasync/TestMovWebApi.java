package test.movasync;

import movasync.MovWebApi;
import movasync.dto.*;
import org.junit.jupiter.api.Test;
import util.HttpRequest;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * MovWebApi tests
 */
public class TestMovWebApi {
    protected int[] count = {0};
    protected MovWebApi movWebApi = new MovWebApi(new HttpRequest().compose(__ -> count[0]++));

    @Test
    public void testSearchDtoNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<SearchResultDto> resultCF = movWebApi.search("Iron Man", 1);

        // Assert
        assertEquals(1, count[0]); // Request made
        assertFalse(resultCF.isDone());

        SearchResultDto result = resultCF.join();
        assertEquals(20, result.getResults().length); // 20 Items per page
    }

    @Test
    public void testGetMovieDtoNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<MovieDto> movieCF = movWebApi.getMovie(120);

        // Assert
        assertEquals(1, count[0]); // Request made
        assertFalse(movieCF.isDone());

        MovieDto movie = movieCF.join();
        assertEquals("The Lord of the Rings: The Fellowship of the Ring", movie.getOriginal_title());
    }

    @Test
    public void testGetPersonDtoNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<PersonDto> personCF = movWebApi.getPerson(3223);

        // Assert
        assertEquals(1, count[0]); // Request made
        assertFalse(personCF.isDone());

        PersonDto person = personCF.join();
        assertEquals("Robert Downey Jr.", person.getName());
        assertEquals("Manhattan, New York, USA", person.getPlace_of_birth());

    }

    @Test
    public void testGetMovieCastDtoNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<CreditsResultDto> movieCastCF = movWebApi.getMovieCast(120);

        // Assert
        assertEquals(1, count[0]); // Request made
        assertFalse(movieCastCF.isDone());

        CreditsResultDto movieCast = movieCastCF.join();
        assertEquals(27, movieCast.getCast().length);
        assertEquals(58, movieCast.getCrew().length);
        assertEquals(120, movieCast.getId());
    }

    @Test
    public void testGetPersonFilmographyNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<SearchItemDto[]> personCreditsCastCF = movWebApi.getPersonCreditsCast(3223);

        // Assert
        assertEquals(1, count[0]); // Request made
        assertFalse(personCreditsCastCF.isDone());

        SearchItemDto[] personCreditsCast = personCreditsCastCF.join();
        assertEquals(101, personCreditsCast.length);
    }
}
