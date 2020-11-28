package test.movlazy.util.stream;

import movlazy.dto.CastDto;
import movlazy.dto.CrewDto;
import movlazy.model.Credit;
import org.junit.jupiter.api.Test;
import util.stream.Queries;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class QueriesTest {
    private static Credit parseCastDto(CastDto castDto, int movId) {
        return new Credit(castDto.getId(),
                movId,
                castDto.getCharacter(),
                castDto.getName(),
                castDto.getCast_id(),
                null);
    }

    // Converts a CrewDto from a movie specified by movId into a Credit
    private static Credit parseCrewDto(CrewDto crewDto, int movId) {
        return new Credit(crewDto.getId(),
                movId,
                crewDto.getName(),
                crewDto.getDepartment(),
                crewDto.getJob(),
                null);
    }

    private static Credit mergeCrewAndCastDtos(CastDto castDto, CrewDto crewDto, int movId) {
        return new Credit(castDto.getId(),
                castDto.getCast_id(),
                movId,
                castDto.getCharacter(),
                castDto.getName(),
                crewDto.getDepartment(),
                crewDto.getJob(),
                null);
    }

    @Test
    public void testMerger() {
        // Arrange
        Stream<String> firstStream = Stream.of("###", "#", "####", "##", "asds", "testtest");
        Stream<Integer> secondStream = Stream.of(1, 2, 3, 4, 5);
        // Act
        Stream<String> actual = Queries.merge(firstStream.spliterator(), secondStream.spliterator(),
                (s, integer) -> s == null ? integer.toString() : integer != null && s.length() == integer ? s : "&&&", String::length, integer -> integer);
        // Assert
        assertIterableEquals(asList("#", "##", "###", "####", "5", "&&&"), actual.collect(Collectors.toList()));
    }

    @Test
    public void testMergerWithModelEntities() {
        // Arrange
        Stream<CrewDto> crewDtoStream = Stream.of(new CrewDto("Design", 1, "Artist", "John"), new CrewDto("Design", 2, "Modeler", "Chris"));
        Stream<CastDto> castDtoStream = Stream.of(new CastDto(20, "Chuck", 1, "John"), new CastDto(21, "Smith", 3, "Jack"));
        Stream<Credit> expected = Stream.of(
                new Credit(1, 20, 21, "Chuck", "John", "Design", "Artist", null),
                new Credit(2, 21, "Chris", "Design", "Modeler", null),
                new Credit(3, 21, "Smith", "Jack", 10, null));
        // Act
        Stream<Credit> actual = Queries.merge(castDtoStream.spliterator(), crewDtoStream.spliterator(), (castDto, crewDto) ->
                        castDto == null ? parseCrewDto(crewDto, 21) :
                                (crewDto == null ? parseCastDto(castDto, 21) : mergeCrewAndCastDtos(castDto, crewDto, 21)),
                CastDto::getId, CrewDto::getId);
        // Assert
        assertIterableEquals(expected.collect(Collectors.toList()), actual.collect(Collectors.toList()));
    }
}
