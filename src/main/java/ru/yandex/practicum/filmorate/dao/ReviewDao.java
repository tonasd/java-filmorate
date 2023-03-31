package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {

    Review create(Review review);

    Review update(Review review);

    void removeReview(long reviewId);

    Review getReviewById(long reviewId);

    List<Review> getReviewsByFilmId(long filmId, int count);

    Review putLike(long reviewId, long userId);

    Review putDislike(long reviewId, long userId);

    void removeLike(long reviewId, long userId);

    void removeDislike(long reviewId, long userId);
}
