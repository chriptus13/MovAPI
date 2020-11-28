package movasync.dto;

/**
 * Class representing a raw Item from a Search Page
 */
public class SearchItemDto {
    private final int id;   // Movie id
    private final String original_title;
    private final String release_date;
    private final double vote_average;

    public SearchItemDto(int id, String original_title, String release_date, double vote_average) {
        this.id = id;
        this.original_title = original_title;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SearchItemDto{" +
                "id='" + id +
                "', original_title='" + original_title +
                "', release_date='" + release_date +
                "', vote_average=" + vote_average +
                "}";
    }
}
