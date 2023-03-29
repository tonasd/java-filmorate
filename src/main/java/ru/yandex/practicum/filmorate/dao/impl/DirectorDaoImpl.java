package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

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
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");
        int id = simpleJdbcInsert.executeAndReturnKey(Map.of("name", director.getName())).intValue();
        return id;
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
    public Director get(int directorId) {
        final String sql = "SELECT director_id, name " +
                "FROM directors " +
                "WHERE director_id = ? ";
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
                "FROM directors ";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM directors " +
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
                "WHERE film_id = ? ";
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
                        ps.setInt(2, directorsArray[i].getId());
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

        director.setId(rs.getInt("director_id"));
        director.setName(rs.getString("name"));

        return director;
    }

}