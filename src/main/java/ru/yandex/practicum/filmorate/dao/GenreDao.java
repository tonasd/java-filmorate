package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre get(int genreId);

    List<Genre> getAll();

    Set<Genre> getGenresForFilmId(long filmId);

    void insertGenresForFilmId(long filmId, Set<Genre> genres);

    void deleteGenresForFilmId(long filmId);
}