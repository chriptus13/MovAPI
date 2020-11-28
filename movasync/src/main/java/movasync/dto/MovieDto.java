package movasync.dto;

import java.util.Arrays;

/**
 * Class representing a raw Movie
 */
public class MovieDto {
    private final int id;
    private final String original_title;
    private final String original_language;
    private final String tagline;
    private final String overview;
    private final double vote_average;
    private final String release_date;
    private final GenreDto[] genres;
    private final String poster_path;

    public MovieDto(int id, String original_title, String original_language, String tagline, String overview, double vote_average, String release_date, GenreDto[] genres, String poster_path) {
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.tagline = tagline;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.genres = genres;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public GenreDto[] getGenres() {
        return genres;
    }

    public String getPoster_path() {
        return poster_path;
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", original_title='" + original_title +
                "', tagline='" + tagline +
                "', genres=" + Arrays.toString(genres) +
                ", overview='" + overview +
                "', original_language='" + original_language +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date +
                "'}";
    }
}