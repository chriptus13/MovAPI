package movlazy.model;

import java.util.Arrays;
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
    private final Supplier<Stream<Credit>> cast;

    public Movie(int id, String originalTitle, String tagline, Supplier<Stream<Genre>> genres,
                 String overview, double voteAverage, String releaseDate, Supplier<Stream<Credit>> cast) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.genres = genres;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.cast = cast;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
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

    public Supplier<Stream<Credit>> getCast() {
        return cast;
    }

    public boolean isOfGenre(String name) {
        return genres.get().anyMatch(genre -> genre.getName().equals(name));
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