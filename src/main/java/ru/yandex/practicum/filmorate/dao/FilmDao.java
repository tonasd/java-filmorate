package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    Film insertFilm(Film film);
    Film updateFilm(Film film);
    Film findFilmById(long filmId);
    List<Film> getAllFilms();
    void addLike(long filmId, long userId);
    void removeLike(long filmId, long userId);
    List<Film> getMostPopular(int size);
}
