package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    private final FilmDao filmDao;

    @Override
    public Film get(long filmId) {
        return filmDao.findFilmById(filmId);
    }

    @Override
    public List<Film> getAll() {
        return filmDao.getAllFilms();
    }

    @Override
    public Film put(Film film) {
        return filmDao.insertFilm(film);
    }

    @Override
    public void update(Film film) {
        filmDao.updateFilm(film);
    }
}
