package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmStorage {
    Film get(long filmId);
    List<Film> getAll();
    Film put(Film film);
    void update(Film film);
    void addLike(long filmId, long userId);
    void removeLike(long filmId, long userId);
    List<Film> getPopular(int size);
    Genre getGenre(int genreId);
    List<Genre> getAllGenres();

    List<Rating> getAllRatings();

    Rating getRating(int ratingId);
}
