package movasync.dto;

/**
 * Class representing a raw Cast credit from a movie
 */
public class CastDto {
    private final int cast_id;      // id inside movie
    private final String character;
    private final int id;           // person id
    private final String name;

    public CastDto(int cast_id, String character, int id, String name) {
        this.cast_id = cast_id;
        this.character = character;
        this.id = id;
        this.name = name;
    }

    public int getCast_id() {
        return cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CastDto{" +
                "id=" + id +
                ", name='" + name +
                "', cast_id=" + cast_id +
                ", character='" + character +
                "'}";
    }
}
