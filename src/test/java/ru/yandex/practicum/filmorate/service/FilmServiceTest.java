package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    static FilmService service;
    static UserService userService;
    static Film expected;
    static User user;
    static Validator validator;

    @BeforeAll
    static void setUp() {
        UserStorage userStorage = new InMemoryUserStorage();
        service = new FilmService(new InMemoryFilmStorage(), userStorage);
        userService = new UserService(userStorage);
        user = new User("user@email.com", "login", "name", LocalDate.now());
        userService.create(user);
        expected = new Film("Snatch", "A film of Guy Ritchie", LocalDate.of(2000, 5, 10), 104);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Test
    void create() {
        service.create(expected);
        final Film actual = service.getAll().get(0);
        assertEquals(expected, actual);
        assertNotEquals(0, actual.getId());

        Film filmWithEmptyName = new Film(expected);
        filmWithEmptyName.setName("");
        assertThrows(ConstraintViolationException.class, () -> validate(filmWithEmptyName));

        Film filmWithDescriptionLongerThan200 = new Film(expected);
        filmWithDescriptionLongerThan200.setDescription("a".repeat(201));
        assertThrows(ConstraintViolationException.class, () -> validate(filmWithDescriptionLongerThan200));

        Film filmWithReleaseDateBeforeCinemaEra = new Film(expected);
        filmWithReleaseDateBeforeCinemaEra.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> service.create(filmWithReleaseDateBeforeCinemaEra));

        Film filmWithWrongDuration = new Film(expected);
        filmWithWrongDuration.setDuration(0);
        assertThrows(ConstraintViolationException.class, () -> validate(filmWithWrongDuration));
        filmWithWrongDuration.setDuration(-1);
        assertThrows(ConstraintViolationException.class, () -> validate(filmWithWrongDuration));
    }

    @Test
    void update() {
        expected.setDescription("ChangedDescription");
        expected = service.update(expected);
        final Film actual = service.getAll().get(0);
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        assertEquals(1, service.getAll().size());
        assertEquals(expected, service.getAll().get(0));
    }

    @Test
    void addLike() {
        service.addLike(expected.getId(), user.getId());
        assertTrue(service.getAll().get(0).getUsersLiked().contains(user.getId()));
        assertEquals(1, service.getAll().get(0).timesLiked());
    }

    @Test
    public void removeLike() {
        service.addLike(expected.getId(), user.getId());
        assertEquals(1, expected.timesLiked());
        service.removeLike(expected.getId(), user.getId());
        assertTrue(service.getAll().get(0).getUsersLiked().isEmpty());
    }

    @Test
    void getPopular() {
        User[] users = new User[20];
        for (int i = 1; i < 20; ++i) {
            Film f = new Film(expected);
            f.setName(String.valueOf(i));
            f = service.create(f);
            User u = new User(user);
            u.setName(String.valueOf(i));
            u = userService.create(u);
            users[i] = u;
            for (int j = 1; j <= i; ++j) {
                service.addLike(f.getId(), users[j].getId());
            }
        }

        List<Film> mostLiked = service.getPopular(5);
        assertEquals(5, mostLiked.size());
        for (int i = 0; i < 5; ++i) {
            assertEquals(20 - i, mostLiked.get(i).timesLiked());
        }
    }

    private void validate(Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}