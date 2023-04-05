package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(Director director) {
        String sql = "INSERT INTO directors (name) " +
                "VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"director_id"});
            stmt.setString(1, director.getName());

            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void update(Director director) {
        final String sql = "UPDATE directors " +
                "SET name = ? " +
                "WHERE director_id = ?";
        boolean updated = jdbcTemplate.update(sql, director.getName(), director.getId()) > 0;
        if (!updated) {
            throw new ItemNotFoundException(String.format("Director with id %d not found", director.getId()));
        }
    }

    @Override
    public Director get(long directorId) {
        final String sql = "SELECT director_id, name " +
                "FROM directors " +
                "WHERE director_id = ? AND (NOT IS_DELETED)";
        Director director;
        try {
            director = jdbcTemplate.queryForObject(sql, this::mapRowToDirector, directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Director with id %d not found", directorId));
        }
        return director;
    }

    @Override
    public List<Director> getAll() {
        final String sql = "SELECT director_id, name " +
                "FROM directors " +
                "WHERE NOT IS_DELETED";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE directors SET IS_DELETED = true " +
                "WHERE director_id = ?";
        boolean deleted = jdbcTemplate.update(sql, id) > 0;
        if (!deleted) {
            throw new ItemNotFoundException(String.format("Director with id %d not found", id));
        }
    }

    public Set<Director> getDirectorsForFilmId(long filmId) {
        final String sql = "SELECT fd.director_id, name " +
                "FROM film_director AS fd " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE d.IS_DELETED = false " +
                "AND film_id = ? ";
        List<Director> directors = jdbcTemplate.query(sql, this::mapRowToDirector, filmId);
        return new HashSet<>(directors);
    }

    public void insertDirectorsForFilmId(long filmId, Set<Director> directors) {
        Director[] directorsArray = directors.toArray(new Director[0]);
        final String sql = "INSERT INTO film_director(film_id, director_id) VALUES(?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, directorsArray[i].getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return directorsArray.length;
                    }
                }
        );
    }

    public void deleteDirectorsForFilmId(long filmId) {
        final String sql = "DELETE FROM film_director " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private Director mapRowToDirector(ResultSet rs, int rowNumber) throws SQLException {
        Director director = new Director();

        director.setId(rs.getLong("director_id"));
        director.setName(rs.getString("name"));

        return director;
    }

}
