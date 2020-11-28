package movasync.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class representing an Person
 */
public class Person {
    private final int id;
    private final String name;
    private final Supplier<CompletableFuture<Stream<SearchItem>>> movies;
    private final String placeOfBirth;
    private final String biography;
    private final String profile_path;

    public Person(int id, String name, Supplier<CompletableFuture<Stream<SearchItem>>> movies, String placeOfBirth, String biography, String profile_path) {
        this.id = id;
        this.name = name;
        this.movies = movies;
        this.placeOfBirth = placeOfBirth;
        this.biography = biography;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getBiography() {
        return biography;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public Supplier<CompletableFuture<Stream<SearchItem>>> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }
}
