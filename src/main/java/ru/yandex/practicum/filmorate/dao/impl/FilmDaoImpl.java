package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public long insertFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateFilm(Film film) {
        final String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ? AND NOT IS_DELETED";
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
                "WHERE film_id = ? AND NOT IS_DELETED";
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
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id " +
                "WHERE NOT IS_DELETED";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public void addLike(long filmId, long userId) {
        final String sql = "INSERT INTO favorite_films(film_id, user_id) " +
                "VALUES(?,?)";
        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (DataIntegrityViolationException e) {
            // случай повторного добавления, ничего не нужно делать и выбрасывать ошибки иначе не пройдёт тесты
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
                "WHERE NOT f.IS_DELETED " +
                "ORDER BY likes DESC " +
                "LIMIT ?;";
        return jdbcTemplate.query(sql, this::mapRowToFilm, size);
    }

    @Override
    public void deleteFilmById(long filmId) {
        final String sql = "UPDATE films SET IS_DELETED = true " +
                "WHERE film_id = ?";
        try {
            jdbcTemplate.update(sql, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Film with id %d not found", filmId));
        }
    }

    public List<Film> getFilmsOfDirectorSortedByLikes(int directorId) {
        final String sql = "SELECT * " +
                "FROM FILM_DIRECTOR AS fd " +
                "JOIN FILMS AS f ON fd.film_id = f.film_id " +
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN " +
                "  (SELECT film_id, " +
                "          COUNT(user_id) AS likes " +
                "   FROM favorite_films " +
                "   GROUP BY film_id) AS l ON fd.film_id = l.film_id " +
                "WHERE director_id = ? AND NOT fd.IS_DELETED " +
                "ORDER BY likes DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    public List<Film> getFilmsOfDirector(int directorId) {
        final String sql = "SELECT * " +
                "FROM FILM_DIRECTOR AS fd " +
                "JOIN FILMS AS f ON fd.film_id = f.film_id " +
                "LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id " +
                "WHERE director_id = ? AND NOT fd.IS_DELETED";
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getRecommendedFilms(long userId) {
        String sql = "SELECT f.*, r.* " +
                "FROM FILMS f " +
                "LEFT JOIN AGE_RESTRICTION_RATINGS AS r ON f.rating_id = r.rating_id " +
                "JOIN FAVORITE_FILMS ff ON f.FILM_ID = ff.FILM_ID " +
                "WHERE ff.USER_ID = ( " +
                "SELECT ff2.USER_ID " +
                "FROM FAVORITE_FILMS ff " +
                "JOIN FAVORITE_FILMS ff2 ON ff.FILM_ID = ff2.FILM_ID " +
                "WHERE ff.USER_ID = SET(@id,?) " +
                "GROUP BY FF2.USER_ID " +
                "HAVING FF2.USER_ID != @id " +
                "ORDER BY COUNT(FF2.*)DESC " +
                "LIMIT 1) " +
                "AND f.FILM_ID NOT IN (SELECT FILM_ID FROM FAVORITE_FILMS WHERE USER_ID = @id) AND NOT f.IS_DELETED";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId);
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        String parametr;
        switch (by) {
            case "director":
                parametr = "(LOWER(d.name) LIKE %s)";
                parametr = String.format(parametr, "'%" + query.toLowerCase(Locale.ROOT) + "%'");
                break;
            case "title":
                parametr = "(LOWER(f.name) LIKE %s)";
                parametr = String.format(parametr, "'%" + query.toLowerCase(Locale.ROOT) + "%'");
                break;
            case "director,title":
            case "title,director":
                parametr = "(LOWER(d.name) LIKE %1$s) OR (LOWER(f.name) LIKE  %1$s)";
                parametr = String.format(parametr, "'%" + query.toLowerCase(Locale.ROOT) + "%'");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + by);
        }

        String sqlQuery =
                "SELECT f.film_id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "f.rating_id, " +
                        "m.rating_block, " +
                        "m.rating_description " +
                "FROM FILMS AS f " +
                "JOIN AGE_RESTRICTION_RATINGS AS m ON m.rating_id = f.rating_id " +
                "LEFT JOIN FILM_DIRECTOR AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN DIRECTORS AS d ON fd.director_id = d.director_id " +
                "LEFT JOIN FAVORITE_FILMS AS l ON f.film_id = l.film_id " +
                "WHERE " + parametr +  " AND NOT f.IS_DELETED " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
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

    private Integer getRatingId(Film film) {
        Rating rating = film.getMpa();
        return rating != null ? rating.getId() : null;
    }
}
