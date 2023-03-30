package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorDao {
    Director get(int directorId);

    List<Director> getAll();

    Set<Director> getDirectorsForFilmId(long filmId);

    void insertDirectorsForFilmId(long filmId, Set<Director> genres);

    void deleteDirectorsForFilmId(long filmId);

    int create(Director director); // returns id of created director in DB

    void update(Director director);

    void delete(int id);
}
