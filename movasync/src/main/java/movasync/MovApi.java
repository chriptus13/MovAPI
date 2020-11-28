package movasync;

import movasync.dto.*;

import java.util.concurrent.CompletableFuture;

/**
 * Interface which specifies the methods a MovApi should have
 */
public interface MovApi {
    /**
     * Returns the movies with the titles which match any word in the String title
     */
    CompletableFuture<SearchResultDto> search(String title, int page);

    /**
     * Returns the movie with the specified id
     */
    CompletableFuture<MovieDto> getMovie(int movieId);

    /**
     * Returns the Cast list for a specified movie
     */
    CompletableFuture<CreditsResultDto> getMovieCast(int movieId);

    /**
     * Returns the person with the specified id
     */
    CompletableFuture<PersonDto> getPerson(int personId);

    /**
     * Returns the movies in which the person with the specified id appears
     */
    CompletableFuture<SearchItemDto[]> getPersonCreditsCast(int personId);
}
