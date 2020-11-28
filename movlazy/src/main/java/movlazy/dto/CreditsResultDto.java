package movlazy.dto;

import java.util.Arrays;

/**
 * Class representing the raw result of Movie Credits list Search
 */
public class CreditsResultDto {
    private final int id;               // Movie id
    private final CastDto[] cast;
    private final CrewDto[] crew;

    public CreditsResultDto(int id, CastDto[] cast, CrewDto[] crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public int getId() {
        return id;
    }

    public CastDto[] getCast() {
        return cast;
    }

    public CrewDto[] getCrew() {
        return crew;
    }

    @Override
    public String toString() {
        return "CreditsResultDto{" +
                "id=" + id +
                ", cast=" + Arrays.toString(cast) +
                '}';
    }
}