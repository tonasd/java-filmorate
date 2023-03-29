package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public long insertFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        long id = simpleJdbcInsert.executeAndReturnKey(this.toMap(film)).longValue();
        return id;
    }

    @Override
    public void updateFilm(Film film) {
        final String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                getRatingId(film),
                film.getId()
        );
    }

    @Override
    public Film findFilmById(long filmId) {
        final String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id " +
                "WHERE film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Film with id %d not found", filmId));
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        final String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public void addLike(long filmId, long userId) {
        final String sql = "INSERT INTO favorite_films(film_id, user_id) " +
                "VALUES(?,?)";
        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (DataIntegrityViolationException e) {
            throw new ItemNotFoundException(
                    String.format(
                            "Film with id=%d or user with id=%d does not exist",
                            filmId,
                            userId)
            );
        }
    }

    @Override
    public void removeLike(long filmId, long userId) {
        final String sql = "DELETE FROM favorite_films " +
                "WHERE film_id = ? AND user_id = ?";
        boolean isDeleted = jdbcTemplate.update(sql, filmId, userId) > 0;
        if (!isDeleted) {
            throw new ItemNotFoundException(
                    String.format(
                            "Not found film id=%d and/or user id=%d",
                            filmId,
                            userId)
            );
        }
    }

    @Override
    public List<Film> getMostPopular(int size) {
        final String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN " +
                "  (SELECT film_id, " +
                "          COUNT(user_id) AS likes " +
                "   FROM favorite_films " +
                "   GROUP BY film_id) AS l ON f.film_id = l.film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?;";
        return jdbcTemplate.query(sql, this::mapRowToFilm, size);
    }

    @Override
    public void deleteFilmById(long filmId) {
        final String sql = "DELETE FROM films " +
                "WHERE film_id = ?";
        try {
            jdbcTemplate.update(sql, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Film with id %d not found", filmId));
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int i) throws SQLException {
        Film film = new Film();

        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setMpa(mapRowToRating(resultSet, i));

        return film;
    }

    private Rating mapRowToRating(ResultSet resultSet, int i) throws SQLException {
        Rating rating = new Rating();

        rating.setId(resultSet.getInt("rating_id"));
        if (rating.getId() == 0) { // if there is no rating_id information about in DB
            return null;
        }
        rating.setName(resultSet.getString("rating_block"));
        rating.setDescription(resultSet.getString("rating_description"));

        return rating;
    }


    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();

        values.put("description", film.getDescription());
        values.put("duration", film.getDuration());
        values.put("name", film.getName());
        values.put("release_date", film.getReleaseDate());
        values.put("rating_id", getRatingId(film));

        return values;
    }

    private Integer getRatingId(Film film) {
        Rating rating = film.getMpa();
        return rating != null ? rating.getId() : null;
    }
}