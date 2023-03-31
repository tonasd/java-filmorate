package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;
    private final FilmService filmService;
    private final UserService userService;

    @Override
    public Review create(Review review) {
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
        String sql = "INSERT INTO REVIEWS (USEFUL, IS_POSITIVE, CONTENT, USER_ID, FILM_ID) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            ps.setInt(1, review.getUseful());
            ps.setBoolean(2, review.getIsPositive());
            ps.setString(3, review.getContent());
            ps.setLong(4, review.getUserId());
            ps.setLong(5, review.getFilmId());
            return ps;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());
        return review;
    }

    @Override
    public Review update(Review review) throws ReviewNotFoundException {
        if (isReviewExist(review.getReviewId())) {
            try {
                String sql = "UPDATE REVIEWS SET " +
                        "IS_POSITIVE = ?, " +
                        "CONTENT = ? " +
                        "WHERE REVIEW_ID = ?";
                jdbcTemplate.update(sql,
                        review.getIsPositive(),
                        review.getContent(),
                        review.getReviewId());
                Review updatedReview = getReviewById(review.getReviewId());
                return updatedReview;
            } catch (EmptyResultDataAccessException e) {
                throw new ReviewNotFoundException("Неверно указан id = " + review.getReviewId() + " отзыва");
            }
        } else {
            throw new ReviewNotFoundException("Неверно указан id = " + review.getReviewId() + " отзыва");
        }
    }

    @Override
    public void removeReview(long reviewId) {
        Review review = getReviewById(reviewId);
        String sql = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Review getReviewById(long reviewId) throws ReviewNotFoundException {
        try {
            String sql = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
            return jdbcTemplate.queryForObject(sql, new ReviewRowMapper(), reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException("Неверно указан id = " + reviewId + " отзыва");
        }
    }

    @Override
    public List<Review> getReviewsByFilmId(long filmId, int count) {
        String sql1 = "SELECT * FROM REVIEWS ORDER BY USEFUL DESC LIMIT ?";
        String sql2 = "SELECT * FROM REVIEWS WHERE FILM_ID=? ORDER BY USEFUL DESC LIMIT ?";
        if (filmId > 0) {
            return jdbcTemplate.query(sql2, new ReviewRowMapper(), filmId, count);
        } else {
            return jdbcTemplate.query(sql1, new ReviewRowMapper(), count);
        }
    }

    @Override
    public Review putLike(long reviewId, long userId) {
        getReviewById(reviewId);
        userService.get(userId);
        String sqlLike = "INSERT INTO REVIEWS_USERS (REVIEW_ID, USER_ID) VALUES (?, ?)";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public Review putDislike(long reviewId, long userId) {
        getReviewById(reviewId);
        userService.get(userId);
        String sqlLike = "INSERT INTO REVIEWS_USERS (REVIEW_ID, USER_ID) VALUES (?, ?)";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public void removeLike(long reviewId, long userId) {
        getReviewById(reviewId);
        userService.get(userId);
        String sqlLike = "DELETE FROM REVIEWS_USERS WHERE REVIEW_ID = ? AND USER_ID = ?";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
    }

    @Override
    public void removeDislike(long reviewId, long userId) {
        getReviewById(reviewId);
        userService.get(userId);
        String sqlLike = "DELETE FROM REVIEWS_USERS WHERE REVIEW_ID = ? AND USER_ID = ?";
        String sqlReviewUseful = "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlLike, reviewId, userId);
        jdbcTemplate.update(sqlReviewUseful, reviewId);
    }

    Boolean isReviewExist(long reviewId) {
        String sql = "SELECT COUNT(*) FROM REVIEWS WHERE REVIEW_ID = ?";
        int check = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        if (check != 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
