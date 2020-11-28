package movasync;

import movasync.dto.*;
import movasync.model.*;
import util.stream.Cache;
import util.stream.Queries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Class which uses MovWebApi to convert raw Dtos into Model versions
 * Also caches entities
 */
public class MovService {
    private final MovApi movWebApi;
    private final Map<Integer, CompletableFuture<Movie>> movies = new HashMap<>();
    private final Map<Integer, CompletableFuture<Supplier<Stream<Credit>>>> cast = new HashMap<>();
    private final Map<Integer, CompletableFuture<Person>> actors = new HashMap<>();

    public MovService(MovApi movWebApi) {
        this.movWebApi = movWebApi;
    }

    public CompletableFuture<Stream<SearchItem>> search(String name) {
        CompletableFuture<SearchResultDto> page1 = movWebApi.search(name, 1);
        return page1.thenApply(searchResultDto -> {
                List<CompletableFuture<Stream<SearchItemDto>>> list = IntStream.rangeClosed(2, searchResultDto.getTotal_pages())
                        .mapToObj(page -> movWebApi.search(name, page).thenApply(searchResultDto1 -> Stream.of(searchResultDto1.getResults())))  // Stream<CompletableFuture<Stream<SearchItemDto>>>
                        .collect(toList());  // List<CompletableFuture<Stream<SearchItemDto>>>
                return Stream.concat(Stream.of(page1.thenApply(searchResultDto1 -> Stream.of(searchResultDto1.getResults()))), list.stream())  // Stream<CompletableFuture<Stream<SearchItemDto>>> pedidos já feitos
                        .flatMap(CompletableFuture::join)   // Stream<SearchItemDto>
                        .map(MovService.this::parseSearchItemDto);   // Stream<SearchItem>
            }
        );
    }

    public CompletableFuture<Movie> getMovie(int movId) {
        return movies.computeIfAbsent(movId, id -> movWebApi.getMovie(movId).thenApply(this::parseMovieDto));
    }

    public CompletableFuture<Stream<Credit>> getMovieCast(int movId) {
        return cast.computeIfAbsent(movId, id ->
                movWebApi.getMovieCast(id)  // CompletableFuture<CreditsResultDto>
                        .thenApply(creditsResultDto -> Cache.of(() -> Queries.merge(Arrays.stream(creditsResultDto.getCast()).spliterator(),
                                Arrays.stream(creditsResultDto.getCrew()).spliterator(),
                                (castDto, crewDto) -> merge(castDto, crewDto, creditsResultDto),
                                CastDto::getId, CrewDto::getId))
                        )  // CompletableFuture<Supplier<Stream<Credit>>>
        ).thenApply(Supplier::get); // CompletableFuture<Stream<Credit>>
    }

    private Credit merge(CastDto castDto, CrewDto crewDto, CreditsResultDto creditsResultDto) {
        return castDto == null ? parseCrewDto(crewDto, creditsResultDto.getId()) :
                (crewDto == null ? parseCastDto(castDto, creditsResultDto.getId()) :
                        mergeCrewAndCastDtos(castDto, crewDto, creditsResultDto.getId()));
    }

    public CompletableFuture<Person> getActor(int actorId) {
        return actors.computeIfAbsent(actorId, id -> movWebApi.getPerson(id).thenApply(this::parsePersonDto));
    }

    public CompletableFuture<Stream<SearchItem>> getActorCreditsCast(int actorId) {
        return movWebApi.getPersonCreditsCast(actorId).thenApply(searchItemDtos -> Stream.of(searchItemDtos).map(this::parseSearchItemDto));
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
                () -> getActorCreditsCast(personDto.getId()),
                personDto.getPlace_of_birth(),
                personDto.getBiography(),
                personDto.getProfile_path());
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

    // Merges a CrewDto with a CastDto into a Credit
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
                () -> getMovieCast(movieDto.getId()),
                movieDto.getPoster_path());
    }
}
