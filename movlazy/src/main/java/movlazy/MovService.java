package movlazy;

import movlazy.dto.*;
import movlazy.model.*;
import util.stream.Cache;
import util.stream.Queries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Class which uses MovWebApi to convert raw Dtos into Model versions
 * Also caches entities
 */
public class MovService {
    private final MovApi movWebApi;
    private final Map<Integer, Movie> movies = new HashMap<>();
    private final Map<Integer, Supplier<Stream<Credit>>> cast = new HashMap<>();
    private final Map<Integer, Person> actors = new HashMap<>();

    public MovService(MovApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public Supplier<Stream<SearchItem>> search(String name) {
        return () -> Stream.iterate(1, prev -> ++prev)
                .map(page -> movWebApi.search(name, page))
                .takeWhile(movies -> movies.length != 0)
                .flatMap(Stream::of)
                .map(this::parseSearchItemDto);
    }

    public Movie getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> parseMovieDto(movWebApi.getMovie(movId)));
    }

    public Supplier<Stream<Credit>> getMovieCast(int movId) {
        return cast.computeIfAbsent(movId, id -> Cache.of(() -> {
            CreditsResultDto movieCast = movWebApi.getMovieCast(id);
            return Queries.merge(Arrays.stream(movieCast.getCast()).spliterator(), Arrays.stream(movieCast.getCrew()).spliterator(), (castDto, crewDto) ->
                            castDto == null ? parseCrewDto(crewDto, movieCast.getId()) :
                                    (crewDto == null ? parseCastDto(castDto, movieCast.getId()) : mergeCrewAndCastDtos(castDto, crewDto, movieCast.getId())),
                    CastDto::getCast_id, CrewDto::getId);
        }));
    }

    public Person getActor(int actorId) {
        return actors.computeIfAbsent(actorId, id -> parsePersonDto(movWebApi.getPerson(id)));
    }

    public Supplier<Stream<SearchItem>> getActorCreditsCast(int actorId) {
        return () -> Stream.of(movWebApi.getPersonCreditsCast(actorId))
                .map(this::parseSearchItemDto);
    }

    // Converts a SearchItemDto into a SearchItem
    private SearchItem parseSearchItemDto(SearchItemDto dto) {
        return new SearchItem(
                dto.getId(),
                dto.getOriginal_title(),
                dto.getRelease_date(),
                dto.getVote_average(),
                () -> getMovie(dto.getId()));
    }

    // Converts a PersonDto into an Person
    private Person parsePersonDto(PersonDto personDto) {
        return new Person(
                personDto.getId(),
                personDto.getName(),
                personDto.getPlace_of_birth(),
                personDto.getBiography(),
                getActorCreditsCast(personDto.getId()));
    }

    // Converts a CreditDto from a movie specified by movId into a Credit
    private Credit parseCastDto(CastDto castDto, int movId) {
        return new Credit(castDto.getId(),
                movId,
                castDto.getCharacter(),
                castDto.getName(),
                castDto.getCast_id(),
                () -> getActor(castDto.getId()));
    }

    // Converts a CrewDto from a movie specified by movId into a Credit
    private Credit parseCrewDto(CrewDto crewDto, int movId) {
        return new Credit(crewDto.getId(),
                movId,
                crewDto.getName(),
                crewDto.getDepartment(),
                crewDto.getJob(),
                () -> getActor(crewDto.getId()));
    }

    private Credit mergeCrewAndCastDtos(CastDto castDto, CrewDto crewDto, int movId) {
        return new Credit(castDto.getId(),
                castDto.getCast_id(),
                movId,
                castDto.getCharacter(),
                castDto.getName(),
                crewDto.getDepartment(),
                crewDto.getJob(),
                () -> getActor(castDto.getId()));
    }

    // Converts a MovieDto into a Movie
    private Movie parseMovieDto(MovieDto movieDto) {
        return new Movie(
                movieDto.getId(),
                movieDto.getOriginal_title(),
                movieDto.getTagline(),
                () -> Arrays.stream(movieDto.getGenres()).map(genreDto -> new Genre(genreDto.getId(), genreDto.getName())), //of(movieDto.getGenres()).map(genreDto -> new Genre(genreDto.getId(), genreDto.getName())),
                movieDto.getOverview(),
                movieDto.getVote_average(),
                movieDto.getRelease_date(),
                getMovieCast(movieDto.getId()));
    }
}
