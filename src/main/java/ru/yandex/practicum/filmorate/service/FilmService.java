package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

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
    private final FeedService feedService;
    private final UserDao userDao;

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
        filmDao.findFilmById(filmId); //check that film exists
        userDao.findUserById(userId); //check that user exists
        filmDao.addLike(filmId, userId);
        feedService.addEvent(Event.builder()
                .eventType(EventType.LIKE)
                .operation(EventOperation.ADD)
                .userId(userId)
                .entityId(filmId)
                .build());
    }

    public void removeLike(long filmId, long userId) {
        filmDao.findFilmById(filmId); //check that film exists
        userDao.findUserById(userId); //check that user exists
        filmDao.removeLike(filmId, userId);
        feedService.addEvent(Event.builder()
                .eventType(EventType.LIKE)
                .operation(EventOperation.REMOVE)
                .userId(userId)
                .entityId(filmId)
                .build());
    }

    public List<Film> getPopular(int size) {
        List<Film> mostPopularFilms = filmDao.getMostPopular(size);
        for (Film film : mostPopularFilms) {
            film.setGenres(genreDao.getGenresForFilmId(film.getId()));
            film.setDirectors(directorDao.getDirectorsForFilmId(film.getId()));
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

    public List<Film> getFilmsByDirector(int directorId, SortBy sortBy) {
        List<Film> filmsOfDirector;
        switch (sortBy) {
            case YEAR:
                filmsOfDirector = filmDao.getFilmsOfDirector(directorId).stream()
                        .sorted(Comparator.comparing(Film::getReleaseDate))
                        .collect(Collectors.toUnmodifiableList());
                break;
            case LIKES:
                filmsOfDirector = filmDao.getFilmsOfDirectorSortedByLikes(directorId);
                break;
            default:
                throw new WrongMethodTypeException(sortBy + " is unknown sortBy type");
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
                .peek(film -> film.setGenres(genreDao.getGenresForFilmId(film.getId())))
                .peek(film -> film.setDirectors(directorDao.getDirectorsForFilmId(film.getId())))
                .collect(Collectors.toList());
    }

    public List<Film> searchFilms(String query, String by) {
        return filmDao.searchFilms(query, by).stream()
                .map(f -> getFilmWithGenresAndDirectors(f.getId()))
                .collect(Collectors.toList());
    }
}
