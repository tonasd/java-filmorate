package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;

    public Review create(Review review) {
        return reviewDao.create(review);
    }

    public Review update(Review review) {
        return reviewDao.update(review);
    }

    public void removeReview(long reviewId) {
        reviewDao.removeReview(reviewId);
    }

    public Review getReviewById(long reviewId) {
        return reviewDao.getReviewById(reviewId);
    }

    public List<Review> getReviewsByFilmId(long filmId, int count) {
        return reviewDao.getReviewsByFilmId(filmId, count);
    }

    public Review putLike(long reviewId, long userId) {
        return reviewDao.putLike(reviewId, userId);
    }

    public Review putDislike(long reviewId, Long userId) {
        return reviewDao.putDislike(reviewId, userId);
    }

    public void removeLike(long reviewId, Long userId) {
        reviewDao.removeLike(reviewId, userId);
    }

    public void removeDislike(long reviewId, Long userId) {
        reviewDao.removeDislike(reviewId, userId);
    }
}
