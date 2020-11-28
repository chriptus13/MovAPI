package movlazy.model;

import java.util.function.Supplier;

/**
 * Class representing a SearchItem
 */
public class SearchItem {
    private final int id;
    private final String title;
    private final String release_date;
    private final double vote_average;
    private final Supplier<Movie> details;

    public SearchItem(
            int id,
            String title,
            String release_date,
            double vote_average,
            Supplier<Movie> details) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public Movie getDetails() {
        return details.get();
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id=" + id +
                ", title='" + title +
                "', release_date='" + release_date +
                "', vote_average=" + vote_average +
                ", details=" + details +
                '}';
    }
}
