package movlazy;

import com.google.gson.Gson;
import movlazy.dto.*;
import util.IRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Class that obtains various results from a given URL,
 * using the MovieWeb API found <a href= "https://www.themoviedb.org/documentation/api">here</a>
 */
public class MovWebApi implements MovApi {
    /**
     * Constants
     * <p>
     * To format messages URLs use {@link java.text.MessageFormat#format(String, Object...)} method.
     */
    private static final String MOVIE_DB_HOST = "https://api.themoviedb.org/3/";
    private static final String MOVIE_DB_SEARCH = "search/movie?api_key={0}&query={1}&page={2,number,#}";
    private static final String MOVIE_DB_MOVIE = "movie/{0,number,#}?api_key={1}";
    private static final String MOVIE_DB_MOVIE_CREDITS = "movie/{0,number,#}/credits?api_key={1}";
    private static final String MOVIE_DB_PERSON = "person/{0,number,#}?api_key={1}";
    private static final String MOVIE_DB_PERSON_CREDITS = "person/{0,number,#}/movie_credits?api_key={1}";

    private static final String KEY_FILE = "key.txt";
    private static final String API_KEY;

    private static final Gson JSON_CONVERTER = new Gson();

    static {
        URL keyFile = ClassLoader.getSystemResource(KEY_FILE);
        if(keyFile == null)
            throw new IllegalStateException("/!\\ You must get an API KEY at www.themoviedb.org/documentation/api and place it in src/main/resources/key.txt");
        else
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(keyFile.openStream()))) {
                API_KEY = reader.readLine();
            } catch(IOException e) {
                throw new UncheckedIOException(e);
            }
    }

    private IRequest request;

    /*
     * Constructors
     */
    public MovWebApi(IRequest req) {
        request = req;
    }

    // E.g. https://api.themoviedb.org/3/search/movie?api_key=***************&query=war+games
    @Override
    public SearchItemDto[] search(String title, int page) {
        String path = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_SEARCH,
                API_KEY, title.replace(' ', '+'), page);
        return JSON_CONVERTER.fromJson(new InputStreamReader(request.getBody(path)), SearchResultDto.class).getResults();
    }

    // E.g. https://api.themoviedb.org/3/movie/860?api_key=***************
    @Override
    public MovieDto getMovie(int movieId) {
        String path = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE, movieId, API_KEY);
        return JSON_CONVERTER.fromJson(new InputStreamReader(request.getBody(path)), MovieDto.class);
    }

    // E.g. https://api.themoviedb.org/3/movie/860/credits?api_key=***************
    @Override
    public CreditsResultDto getMovieCast(int movieId) {
        String path = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_MOVIE_CREDITS, movieId, API_KEY);
        return JSON_CONVERTER.fromJson(new InputStreamReader(request.getBody(path)), CreditsResultDto.class);
    }

    // E.g. https://api.themoviedb.org/3/person/4756?api_key=***************
    @Override
    public PersonDto getPerson(int personId) {
        String path = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON, personId, API_KEY);
        return JSON_CONVERTER.fromJson(new InputStreamReader(request.getBody(path)), PersonDto.class);
    }

    // E.g. https://api.themoviedb.org/3/person/4756/movie_credits?api_key=***************
    @Override
    public SearchItemDto[] getPersonCreditsCast(int personId) {
        String path = MessageFormat.format(MOVIE_DB_HOST + MOVIE_DB_PERSON_CREDITS, personId, API_KEY);
        return JSON_CONVERTER.fromJson(new InputStreamReader(request.getBody(path)), PersonCastResultDto.class).getCast();
    }
}
