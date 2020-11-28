package movasync.dto;

/**
 * Class representing a raw Person
 */
public class PersonDto {
    private final int id;
    private final String name;
    private final String place_of_birth;
    private final String biography;
    private final String profile_path;

    public PersonDto(int id, String name, String place_of_birth, String biography, String profile_path) {
        this.id = id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.biography = biography;
        this.profile_path = profile_path;
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

    public String getProfile_path() {
        return profile_path;
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
