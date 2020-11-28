package movasync.model;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class representing a Movie
 */
public class Movie {
    private final int id;
    private final String originalTitle;
    private final String tagline;
    private final Supplier<Stream<Genre>> genres;
    private final String overview;
    private final double voteAverage;
    private final String releaseDate;
    private final Supplier<CompletableFuture<Stream<Credit>>> cast;
    private final String poster_path;

    public Movie(int id, String originalTitle, String tagline, Supplier<Stream<Genre>> genres,
                 String overview, double voteAverage, String releaseDate,
                 Supplier<CompletableFuture<Stream<Credit>>> cast, String poster_path) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.genres = genres;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.cast = cast;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public Supplier<Stream<Genre>> getGenres() {
        return genres;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Supplier<CompletableFuture<Stream<Credit>>> getCast() {
        return cast;
    }

    public boolean isOfGenre(String name) {
        return genres.get().anyMatch(genre -> genre.getName().equals(name));
    }

    public String getPoster_path() {
        return poster_path;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", originalTitle='" + originalTitle +
                "', tagline='" + tagline +
                "', genres=" + Arrays.toString(genres.get().toArray()) +
                ", overview='" + overview +
                "', voteAverage=" + voteAverage +
                ", releaseDate='" + releaseDate +
                "'}";
    }
}