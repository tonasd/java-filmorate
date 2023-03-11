package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Genre get(int genreId) {
        final String sql = "SELECT genre_id, name " +
                "FROM genres " +
                "WHERE genre_id = ? ";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sql, this::mapRowToGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Genre with id %d not found", genreId));
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        final String sql = "SELECT genre_id, name " +
                "FROM genres ";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    public Set<Genre> getGenresForFilmId(long filmId) {
        final String sql = "SELECT fg.genre_id, name " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE film_id = ? ";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, filmId);
        return new LinkedHashSet<>(genres);
    }

    public void insertGenresForFilmId(long filmId, Set<Genre> genres) {
        Genre[] genresArray = genres.toArray(new Genre[0]);
        final String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES(?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setInt(2, genresArray[i].getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genresArray.length;
                    }
                }
        );
    }

    public void deleteGenresForFilmId(long filmId) {
        final String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNumber) throws SQLException {
        Genre genre = new Genre();

        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("name"));

        return genre;
    }

}
