package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film get(long filmId);
    List<Film> getAll();
    void put(Film film);
    void update(Film film);
}