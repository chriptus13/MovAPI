package movlazy;

import movlazy.dto.CreditsResultDto;
import movlazy.dto.MovieDto;
import movlazy.dto.PersonDto;
import movlazy.dto.SearchItemDto;

/**
 * Interface which specifies the methods a MovApi should have
 */
public interface MovApi {
    /**
     * Returns the movies with the titles which match any word in the String title
     */
    SearchItemDto[] search(String title, int page);

    /**
     * Returns the movie with the specified id
     */
    MovieDto getMovie(int movieId);

    /**
     * Returns the Cast list for a specified movie
     */
    CreditsResultDto getMovieCast(int movieId);

    /**
     * Returns the person with the specified id
     */
    PersonDto getPerson(int personId);

    /**
     * Returns the movies in which the person with the specified id appears
     */
    SearchItemDto[] getPersonCreditsCast(int personId);
}
