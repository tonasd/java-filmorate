package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final RatingDao ratingDao;

    public Film create(Film film) {
        validate(film);
        long filmId = filmDao.insertFilm(film);
        genreDao.insertGenresForFilmId(filmId, film.getGenres());
        return getFilmWithGenres(filmId);
    }

    public Film update(Film film) {
        validate(film);
        long filmId = film.getId(); // to escape situation if new state of film have fewer genres than current DB state
        genreDao.deleteGenresForFilmId(filmId);
        filmDao.updateFilm(film);
        genreDao.insertGenresForFilmId(filmId, film.getGenres());
        return getFilmWithGenres(filmId);
    }

    public Film get(long filmId) {
        return getFilmWithGenres(filmId);
    }

    public List<Film> getAll() {
        return filmDao.getAllFilms().stream()
                .peek(film -> film.setGenres(genreDao.getGenresForFilmId(film.getId())))
                .collect(Collectors.toList());
    }

    public void addLike(long filmId, long userId) {
        filmDao.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmDao.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int size) {
        return filmDao.getMostPopular(size);
    }

    public Genre getGenre(int genreId) {
        return genreDao.get(genreId);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    public List<Rating> getAllRatings() {
        return ratingDao.getAll();
    }

    public Rating getRating(int ratingId) {
        return ratingDao.get(ratingId);
    }

    private void validate(@NonNull Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Date of the film release is incorrect(before the very first screening)");
        }
    }

    private Film getFilmWithGenres(long filmId) {
        Film film = filmDao.findFilmById(filmId);
        film.setGenres(genreDao.getGenresForFilmId(filmId));
        return film;
    }
}
