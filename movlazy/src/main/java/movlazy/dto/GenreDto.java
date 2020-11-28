package movlazy.dto;

/**
 * Class representing a raw Genre
 */
public class GenreDto {
    private final int id;
    private final String name;

    public GenreDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GenreDto{" +
                "id=" + id +
                ", name='" + name +
                "'}";
    }
}