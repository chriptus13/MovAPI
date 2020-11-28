package test.movasync;

import movasync.MovService;
import movasync.model.Credit;
import movasync.model.Movie;
import movasync.model.Person;
import movasync.model.SearchItem;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MovService Tests
 */
public class TestMovService extends TestMovWebApi {
    private MovService movService = new MovService(movWebApi);

    @Test
    public void testCreditsCache() {
        // Arrange

        // Act
        movService.getMovieCast(120).join();
        movService.getMovieCast(120).join();

        // Assert
        assertEquals(1, count[0]);  // Should only make 1 request to the API
    }

    @Test
    public void testPersonCache() {
        // Arrange

        // Act
        movService.getActor(420).join();
        movService.getActor(420).join();

        // Assert
        assertEquals(1, count[0]);
    }

    @Test
    public void testMovieCache() {
        // Arrange

        // Act
        movService.getMovie(120).join();
        movService.getMovie(120).join();

        // Assert
        assertEquals(1, count[0]);
    }

    @Test
    public void testSearchMultiplePages() {
        // Arrange

        // Act
        movService.search("Iron Man").join();

        // Assert
        assertEquals(3, count[0]);
    }

    @Test
    public void testSearchSinglePage() {
        // Arrange


        // Act
        movService.search("Galinha").join();

        // Assert
        assertEquals(1, count[0]);
    }

    @Test
    public void testEmptySearch() {
        // Arrange

        // Act
        Stream<SearchItem> slbEmpty = movService.search("SLB").join();

        // Assert
        assertEquals(1, count[0]);
        assertEquals(0, slbEmpty.count());
    }

    @Test
    public void testBigSearchNotBlocking() {
        // Arrange
        final int TOTAL_PAGES = 10;

        // Act & Assert
        CompletableFuture<Stream<SearchItem>> spider = movService.search("Spider"); // search should only register the requests
        System.out.println("Requests made!");
        assertTrue(count[0] < TOTAL_PAGES);
        assertFalse(spider.isDone());
        Stream<SearchItem> searchItemStream = spider.join();
        assertEquals(TOTAL_PAGES, count[0]);
        assertEquals(searchItemStream.count(), 193);
    }

    @Test
    public void testGetMovieNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<Movie> movieCF = movService.getMovie(120);

        // Assert
        assertEquals(1, count[0]); // Request posted
        assertFalse(movieCF.isDone());

        Movie movie = movieCF.join();
        assertEquals(120, movie.getId());
    }

    @Test
    public void testGetPersonNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<Person> actorCF = movService.getActor(3223);

        // Assert
        assertEquals(1, count[0]); // Request posted
        assertFalse(actorCF.isDone());

        Person actor = actorCF.join();
        assertEquals(3223, actor.getId());
    }

    @Test
    public void testGetActorFilmographyNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<Stream<SearchItem>> actorCreditsCastCF = movService.getActorCreditsCast(3223);

        // Assert
        assertEquals(1, count[0]); // Request posted
        assertFalse(actorCreditsCastCF.isDone());

        Stream<SearchItem> actorCreditsCast = actorCreditsCastCF.join();
        assertEquals(101, actorCreditsCast.count());
    }

    @Test
    public void testGetMovieCastNotBlocking() {
        // Arrange

        // Act
        CompletableFuture<Stream<Credit>> movieCastCF = movService.getMovieCast(120);

        // Assert
        assertEquals(1, count[0]); // Request posted
        assertFalse(movieCastCF.isDone());

        Stream<Credit> movieCast = movieCastCF.join();
        assertEquals(84, movieCast.count());
    }

    @Test
    public void testSearchThenMovies() {
        // Arrange

        // Act & Assert
        List<SearchItem> searchItemList = movService.search("Iron Man").join().collect(toList());
        assertEquals(3, count[0]); // 3 Pages

        searchItemList.get(0).getDetails().get().join();
        assertEquals(4, count[0]); // 1 more for movie request
    }

    @Test
    public void testSearchThenMovieThenActorThenMovieAgain() {
        // Arrange

        // Act & Assert
        List<SearchItem> searchItemList = movService.search("Iron Man").join().collect(toList());
        assertEquals(3, count[0]); // 3 Pages

        Movie movie = searchItemList.get(0).getDetails().get().join();
        assertEquals(4, count[0]); // 1 more for movie

        assertEquals(1726, movie.getId());
        assertEquals("Heroes aren't born. They're built.", movie.getTagline());
        assertEquals("After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.",
                movie.getOverview());
        assertEquals(7.5, movie.getVoteAverage());

        List<Credit> cast = movie.getCast().get().join().collect(toList());
        assertEquals(5, count[0]); // 1 more for movie cast

        Credit creditIronMan = cast.stream().filter(credit -> credit.getName().equals("Robert Downey Jr.")).findFirst().get();
        Person personIronMan = creditIronMan.getActor().get().join();
        assertEquals(6, count[0]); // 1 more for person
        assertEquals(3223, personIronMan.getId());

        searchItemList = personIronMan.getMovies().get().join().collect(toList());
        assertEquals(7, count[0]); // 1 more for person filmography

        SearchItem searchItem = searchItemList.stream().filter(item -> item.getId() == 1726).findFirst().get();
        assertEquals("Iron Man", searchItem.getTitle());

        movie = searchItem.getDetails().get().join();
        assertEquals(7, count[0]); // no more request because movie already in cache
        assertEquals("Iron Man", movie.getOriginalTitle());
    }

    @Test
    public void testMovieNotFound() {
        // Arrange

        // Act
        Movie movie = movService.getMovie(999999).join();

        // Assert
        assertEquals(0, movie.getId());
    }

    @Test
    public void testMergeCredits() {
        // Arrange

        // Act & Assert
        Movie movie = movService.search("Good Will Hunting").join().findFirst().get().getDetails().get().join();
        assertEquals(2, count[0]); // 2 Requests for search page and movie

        Credit credit = movie.getCast().get().join().filter(credit1 -> credit1.getId() == 1892).findFirst().get();
        assertEquals(3, count[0]); // 1 more request for movie cast

        assertEquals("Matt Damon", credit.getName());
        assertEquals("Will Hunting", credit.getCharacter());
        assertEquals("Writing", credit.getDepartment());
        assertEquals("Writer", credit.getJob());
    }
}
