package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final RatingDao ratingDao;

    @Override
    public Film get(long filmId) {
        Film film = filmDao.findFilmById(filmId);
        film.setGenres(genreDao.getGenresForFilmId(filmId));
        return film;
    }

    @Override
    public List<Film> getAll() {
        return filmDao.getAllFilms().stream()
                .peek(film -> film.setGenres(genreDao.getGenresForFilmId(film.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Film put(Film film) {
        long filmId = filmDao.insertFilm(film);
        genreDao.insertGenresForFilmId(filmId , film.getGenres());
        return get(filmId);
    }

    @Override
    public void update(Film film) {
        long filmId = film.getId(); // to escape situation if new state of film have less genres than current DB state
        genreDao.deleteGenresForFilmId(filmId);
        filmDao.updateFilm(film);
        genreDao.insertGenresForFilmId(filmId, film.getGenres());
    }

    @Override
    public void addLike(long filmId, long userId) {
        filmDao.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmDao.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int size) {
        return filmDao.getMostPopular(size);
    }

    @Override
    public Genre getGenre(int genreId) {
        return genreDao.get(genreId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingDao.getAll();
    }

    @Override
    public Rating getRating(int ratingId) {
        return ratingDao.get(ratingId);
    }
}
