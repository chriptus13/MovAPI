package movlazy.model;

import java.util.function.Supplier;

/**
 * Class representing a Credit
 */
public class Credit {
    private final int id;
    private final int cast_id;
    private final int movieId;
    private final String character;
    private final String name;
    private final String department;
    private final String job;
    private final Supplier<Person> actor;

    // CastDto
    public Credit(int id, int movieId, String character, String name, int cast_id, Supplier<Person> actor) {
        this(id, cast_id, movieId, character, name, null, null, actor);
    }

    // CrewDto
    public Credit(int id, int movieId, String name, String department, String job, Supplier<Person> actor) {
        this(id, -1, movieId, null, name, department, job, actor);
    }

    // CastDto + CrewDto
    public Credit(int id, int cast_id, int movieId, String character, String name, String department, String job, Supplier<Person> actor) {
        this.id = id;
        this.cast_id = cast_id;
        this.movieId = movieId;
        this.character = character;
        this.name = name;
        this.department = department;
        this.job = job;
        this.actor = actor;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public int getMovieId() {
        return movieId;
    }

    public Person getActor() {
        return actor.get();
    }

    public int getCast_id() {
        return cast_id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && ((Credit) obj).id == id;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", character='" + character + '\'' +
                ", name='" + name + '\'' +
                ", getPersonCreditsCast=" + actor +
                '}';
    }
}