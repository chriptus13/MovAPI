package movlazy.dto;

import java.util.Arrays;

/**
 * Class representing a raw Search Page
 */
public class SearchResultDto {
    private final int page;
    private final int total_results;
    private final int total_pages;
    private final SearchItemDto[] results;

    public SearchResultDto(int page, int total_results, int total_pages, SearchItemDto[] results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public SearchItemDto[] getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "SearchResultDto{" +
                "page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", results=" + Arrays.toString(results) +
                '}';
    }
}
