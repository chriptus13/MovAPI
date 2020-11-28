package movlazy.dto;

/**
 * Class representing a raw Person
 */
public class PersonDto {
    private final int id;
    private final String name;
    private final String place_of_birth;
    private final String biography;

    public PersonDto(int id, String name, String placeOfBirth, String biography) {
        this.id = id;
        this.name = name;
        this.place_of_birth = placeOfBirth;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getBiography() {
        return biography;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", name='" + name +
                "', place_of_birth='" + place_of_birth +
                "', biography='" + biography + "'}";
    }
}
