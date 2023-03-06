package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>(); //key=filmId

    @Override
    public Film get(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new ItemNotFoundException(String.format("Film %d not found", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film put(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if (films.replace(film.getId(), film) == null) {
            throw new ItemNotFoundException(String.format("Film with id %d does not exist", film.getId()));
        }
    }
}
