package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    long insertFilm(Film film); //returns filmId

    void updateFilm(Film film);

    Film findFilmById(long filmId);

    List<Film> getAllFilms();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getMostPopular(int size);

    List<Film> getFilmsOfDirector(int directorId);

    List<Film> getFilmsOfDirectorSortedByLikes(int directorId);

    void deleteFilmById(long filmId);
}