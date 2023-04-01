package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.lang.invoke.WrongMethodTypeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final RatingDao ratingDao;
    private final DirectorDao directorDao;

    public Film create(Film film) {
        validate(film);
        long filmId = filmDao.insertFilm(film);
        genreDao.insertGenresForFilmId(filmId, film.getGenres());
        directorDao.insertDirectorsForFilmId(filmId, film.getDirectors());
        return getFilmWithGenresAndDirectors(filmId);
    }

    public Film update(Film film) {
        validate(film);
        long filmId = film.getId(); // to escape situation if new state of film have fewer genres than current DB state
        genreDao.deleteGenresForFilmId(filmId);
        directorDao.deleteDirectorsForFilmId(filmId);
        filmDao.updateFilm(film);
        genreDao.insertGenresForFilmId(filmId, film.getGenres());
        directorDao.insertDirectorsForFilmId(filmId, film.getDirectors());
        return getFilmWithGenresAndDirectors(filmId);
    }

    public Film get(long filmId) {
        return getFilmWithGenresAndDirectors(filmId);
    }

    public List<Film> getAll() {
        return filmDao.getAllFilms().stream()
                .peek(film -> film.setGenres(genreDao.getGenresForFilmId(film.getId())))
                .peek(film -> film.setDirectors(directorDao.getDirectorsForFilmId(film.getId())))
                .collect(Collectors.toList());
    }

    public void addLike(long filmId, long userId) {
        filmDao.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmDao.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int size) {
        List<Film> mostPopularFilms = filmDao.getMostPopular(size);
        for (Film film : mostPopularFilms) {
            film.setGenres(genreDao.getGenresForFilmId(film.getId()));
        }
        return mostPopularFilms;
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

    public void deleteFilmById(Long filmId) {
        filmDao.deleteFilmById(filmId);
    }

    public List<Film> getFilmsByDirector(int directorId, String sortType) {
        List<Film> filmsOfDirector;
        switch (sortType) {
            case "year":
                filmsOfDirector = filmDao.getFilmsOfDirector(directorId).stream()
                        .sorted(Comparator.comparing(Film::getReleaseDate))
                        .collect(Collectors.toUnmodifiableList());
                break;
            case "likes":
                filmsOfDirector = filmDao.getFilmsOfDirectorSortedByLikes(directorId);
                break;
            default:
                throw new WrongMethodTypeException(sortType + " is unknown sortBy type");
        }

        return filmsOfDirector.stream()
                .peek(film -> film.setGenres(genreDao.getGenresForFilmId(film.getId())))
                .peek(film -> film.setDirectors(directorDao.getDirectorsForFilmId(film.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    private void validate(@NonNull Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Date of the film release is incorrect(before the very first screening)");
        }
    }

    private Film getFilmWithGenresAndDirectors(long filmId) {
        Film film = filmDao.findFilmById(filmId);
        film.setGenres(genreDao.getGenresForFilmId(filmId));
        film.setDirectors(directorDao.getDirectorsForFilmId(filmId));
        return film;
    }

    public List<Film> getRecommendations(Long userId) {
        return filmDao.getRecommendedFilms(userId).stream()
                .map(this::getFilmWithGenresAndDirectors)
                .collect(Collectors.toList());
    }
}
