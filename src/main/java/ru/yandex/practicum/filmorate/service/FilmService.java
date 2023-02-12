package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long nextId = 1;

    public Film create(Film film) {
        validate(film);
        film.setId(nextId++);
        filmStorage.put(film);
        return film;
    }

    public Film update(Film film) {
        validate(film);
        filmStorage.update(film);
        return film;
    }

    public Film get(long filmId) {
        return filmStorage.get(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }
    
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userId);
        film.getUsersLiked().add(user.getId());
    }

    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userId);
        film.getUsersLiked().remove(user.getId());
    }

    public List<Film> getPopular(int size) {
        Comparator<Film> cmp = Comparator.comparing(Film::timesLiked).reversed();
        return filmStorage.getAll().stream().sorted(cmp).limit(size).collect(Collectors.toList());
    }

    private void validate(@NonNull Film film) {
        String exceptionText = "";
        if (film.getName().isBlank()) {
            exceptionText += "Film's name cannot be empty";
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Film description should be not longer than 200 characters";
        }
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Date of the film release is incorrect(before the very first screening)";
        }
        if (film.getDuration() <= 0) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Duration of a film must be positive";
        }

        if (!exceptionText.isEmpty()) {
            ValidationException exception = new ValidationException(exceptionText);
            log.warn("", exception);
            throw exception;
        }
    }
}
