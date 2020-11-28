package movlazy.dto;

import java.util.Arrays;

/**
 * Class representing the raw result of Person Cast list Search
 */
public class PersonCastResultDto {
    private final int id;                 // Person id
    private final SearchItemDto[] cast;   // Movies in which the person participates

    public PersonCastResultDto(int id, SearchItemDto[] cast) {
        this.id = id;
        this.cast = cast;
    }

    public int getId() {
        return id;
    }

    public SearchItemDto[] getCast() {
        return cast;
    }

    @Override
    public String toString() {
        return "PersonCastResultDto{" +
                "id=" + id +
                ", cast=" + Arrays.toString(cast) +
                '}';
    }
}
