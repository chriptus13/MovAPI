package movlazy.dto;

/**
 * Class representing a raw Crew credit from a movie
 */
public class CrewDto {
    private final String department;
    private final int id;   // person id
    private final String job;
    private final String name;

    public CrewDto(String department, int id, String job, String name) {
        this.department = department;
        this.id = id;
        this.job = job;
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CrewDto{" +
                "id=" + id +
                ", name='" + name +
                "', department='" + department +
                "', job='" + job +
                "'}";
    }
}
