package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Qualifier("filmStorage")
public class FilmDbStorage implements FilmStorage{
    @Override
    public Film get(long filmId) {
        return null;
    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public void put(Film film) {

    }

    @Override
    public void update(Film film) {

    }
}
