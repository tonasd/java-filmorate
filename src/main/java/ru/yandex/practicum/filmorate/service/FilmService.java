package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film create(Film film) {
        validate(film);
        return filmStorage.put(film);
    }

    public Film update(Film film) {
        validate(film);
        filmStorage.update(film);
        return filmStorage.get(film.getId());
    }

    public Film get(long filmId) {
        return filmStorage.get(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }
    
    public void addLike(long filmId, long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int size) {
        return filmStorage.getPopular(size);
    }

    public Genre getGenre(int genreId) {
        return filmStorage.getGenre(genreId);
    }
    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public List<Rating> getAllRatings() {
        return filmStorage.getAllRatings();
    }

    public Rating getRating(int ratingId) {
        return filmStorage.getRating(ratingId);
    }

    private void validate(@NonNull Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Date of the film release is incorrect(before the very first screening)");
        }
    }
}
