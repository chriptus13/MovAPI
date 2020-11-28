package test;

import com.google.common.util.concurrent.RateLimiter;
import movlazy.MovService;
import movlazy.MovWebApi;
import movlazy.model.Credit;
import movlazy.model.Movie;
import movlazy.model.Person;
import movlazy.model.SearchItem;
import org.junit.jupiter.api.Test;
import util.HttpRequest;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MovServiceTestForDisneyAnimation {
    private static final String MOVIE_ANIMATION_GENRE = "Animation";

    private final int[] count = {0};
    private final RateLimiter RATE_LIMITER = RateLimiter.create(3.0);
    private final MovService movApi = new MovService(new MovWebApi(new HttpRequest()
            .compose(__ -> count[0]++)
            .compose(System.out::println)
            .compose(__ -> RATE_LIMITER.acquire())));

    @Test   // WARNING: LONG TEST
    public void testSearchAnimationMoviesFromDisney() {
        Stream<Movie> movies = movApi.search("Disney").get()
                .filter(searchItem -> searchItem.getDetails().isOfGenre(MOVIE_ANIMATION_GENRE))
                .map(SearchItem::getDetails);
        movies.forEach(System.out::println);
        assertEquals(390, count[0]);    // 20 requests for the pages + 370 requests for the movies
    }

    @Test
    public void testSearchMovieInSinglePage() {
        Movie mov = movApi.search("Mulan").get().iterator().next().getDetails();

        assertEquals(2, count[0]); //1 request for search 1 for getMovie

        assertEquals("A tomboyish girl disguises herself as a young man so she " +
                "can fight with the Imperial Chinese Army against the invading Huns. With help from " +
                "wise-cracking dragon Mushu, Mulan just might save her country -- and win the heart of handsome Captain Li Shang.", mov.getOverview());
        assertEquals("Mulan", mov.getTitle());
        assertEquals(10674, mov.getId());
    }

    @Test
    public void testSearchMovieInMultiplePages() {
        Supplier<Stream<SearchItem>> items = movApi.search("Frozen");

        assertEquals(0, count[0]); // None consumed yet
        for(Iterator<SearchItem> it = items.get().iterator(); it.hasNext(); it.next()) ; // Consume all pages
        assertEquals(7, count[0]); // 7 pages consumed

        assertEquals(107, items.get().count());
    }

    @Test
    public void testMovieCache() {
        final int POCAHONTAS_ID = 10530;

        movApi.getMovie(POCAHONTAS_ID);
        assertEquals(1, count[0]);  // 1 request for movie

        Movie movie = movApi.getMovie(POCAHONTAS_ID);
        assertEquals(1, count[0]);  // +0 requests because movie already in map

        assertEquals("Pocahontas", movie.getTitle());
        assertEquals(POCAHONTAS_ID, movie.getId());
        assertTrue(movie.isOfGenre(MOVIE_ANIMATION_GENRE));
    }

    @Test
    public void testCastCache() {
        final int POCAHONTAS_ID = 10530;

        movApi.getMovieCast(POCAHONTAS_ID);
        assertEquals(0, count[0]); // 0 because not consumed yet

        Stream<Credit> movieCast = movApi.getMovieCast(POCAHONTAS_ID).get();
        assertEquals(1, count[0]);  // +1 because consumed now

        assertEquals(47, movieCast.count());
    }

    @Test
    public void testActorCache() {
        final int POCAHONTAS_VOICE_ACTOR_ID = 65529;

        movApi.getActor(POCAHONTAS_VOICE_ACTOR_ID);
        assertEquals(1, count[0]);  // 1 request for actor

        Person actor = movApi.getActor(POCAHONTAS_VOICE_ACTOR_ID);
        assertEquals(1, count[0]);  // +0 requests because actor already in map

        assertEquals("Irene Bedard", actor.getName());
    }

    @Test
    public void testActorCast() {
        final int MUSHU_VOICE_ACTOR_ID = 776;   // Also Eddie Murphy xD

        Stream<SearchItem> movieCast = movApi.getActorCreditsCast(MUSHU_VOICE_ACTOR_ID).get();
        assertEquals(1, count[0]);  // 1 request for movie cast

        assertEquals("Beverly Hills Cop", movieCast.iterator().next().getTitle());
    }

    @Test
    public void testSearchMovieThenMovieThenCastThenActorThenMoviesAgain() {
        SearchItem firstItem = movApi.search("Mulan").get().iterator().next();
        assertEquals(1, count[0]);  // 1 request for search in first page

        // Get movie from first item
        Movie movie = firstItem.getDetails();
        assertEquals(2, count[0]);  // 1 request for movie
        assertEquals("Mulan", movie.getTitle());
        assertTrue(movie.isOfGenre(MOVIE_ANIMATION_GENRE));
        assertEquals("A tomboyish girl disguises herself as a young man so she can fight with the " +
                "Imperial Chinese Army against the invading Huns. With help from wise-cracking dragon Mushu, " +
                "Mulan just might save her country -- and win the heart of handsome Captain Li Shang.", movie.getOverview());

        // Get movie cast from movie
        Supplier<Stream<Credit>> cast = movie.getCast();
        assertEquals(2, count[0]);  // not consumed

        // Get first castItem
        final int MULAN_ID = 21702;
        Credit mulanCast = cast.get().filter(credit -> credit.getId() == MULAN_ID).iterator().next();
        assertEquals(3, count[0]);  // consumed
        assertEquals("Mulan (voice)", mulanCast.getCharacter());
        assertEquals("Ming-Na Wen", mulanCast.getName());

        // Get actor from castItem
        Person mulanActor = mulanCast.getActor();
        assertEquals(4, count[0]);  // 1 request for actor
        assertEquals("Ming-Na Wen", mulanActor.getName());
        assertEquals(21702, mulanActor.getId());
        assertEquals("Coloane Island, Macau", mulanActor.getPlaceOfBirth());

        // Get movies for actor
        Stream<SearchItem> mulanActorMovies = mulanActor.getMovies().get();
        assertEquals(5, count[0]);  // 1 request for actor movie cast
        assertEquals("Mulan", mulanActorMovies.skip(1).iterator().next().getTitle());
    }

    @Test
    public void testEmptyRequest() {
        assertThrows(RuntimeException.class,
                () -> movApi.getMovie(1111111111),   // Invalid movie ID -> Page Not Found
                "FileNotFoundException");
    }
}