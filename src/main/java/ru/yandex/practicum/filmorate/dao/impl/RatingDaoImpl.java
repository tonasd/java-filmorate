package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RatingDaoImpl implements RatingDao {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Rating> getAll() {
        final String sql = "SELECT * " +
                "FROM age_restriction_ratings";
        return jdbcTemplate.query(sql, this::mapRowToRating);
    }

    @Override
    public Rating get(int ratingId) {
        final String sql = "SELECT * " +
                "FROM age_restriction_ratings " +
                "WHERE rating_id = ?";
        Rating rating;
        try {
           rating = jdbcTemplate.queryForObject(sql, this::mapRowToRating, ratingId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("Rating with id %d not found", ratingId));
        }
        return rating;
    }

    private Rating mapRowToRating(ResultSet rs, int rowNumber) throws SQLException {
        Rating rating = new Rating();

        rating.setId(rs.getInt("rating_id"));
        rating.setName(rs.getString("rating_block"));
        rating.setDescription(rs.getString("rating_description"));

        return rating;
    }
}
