package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController controller;
    Film expected;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
        expected = new Film("Snatch", "A film of Guy Ritchie", LocalDate.of(2000, 5, 10), Duration.ofMinutes(104));

    }

    @Test
    void createAndUpdate() {
        controller.create(expected);
        Film actual = controller.getAll().get(0);
        assertEquals(expected, actual);
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertNotEquals(0, actual.getId());
        expected.setDescription("ChangedDescription");
        controller.update(expected);
        actual = controller.getAll().get(0);
        assertEquals(expected, actual);
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void isCorrect() {
        Film filmWithEmptyName = expected.toBuilder().name("").build();
        assertThrows(ValidationException.class, () -> controller.isCorrect(filmWithEmptyName));

        Film filmWithDescriptionLongerThan200 = expected.toBuilder().description("a".repeat(201)).build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(filmWithDescriptionLongerThan200));

        Film filmWithReleaseDateBeforeCinemaEra = expected.toBuilder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(filmWithReleaseDateBeforeCinemaEra));

        Film filmWithZeroDuration = expected.toBuilder().duration(Duration.ZERO).build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(filmWithZeroDuration));
        Film filmWithNegativeDuration = expected.toBuilder().duration(Duration.ZERO.minusSeconds(1)).build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(filmWithNegativeDuration));

    }
}