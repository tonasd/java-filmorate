package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final FeedService feedService;

    public Review create(Review review) {
        Review createReview = reviewDao.create(review);
        feedService.addEvent(Event.builder()
                .eventType(EventType.REVIEW)
                .operation(EventOperation.ADD)
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .build());
        return createReview;
    }

    public Review update(Review review) {
        Review updateReview = reviewDao.update(review);
        feedService.addEvent(Event.builder()
                .eventType(EventType.REVIEW)
                .operation(EventOperation.UPDATE)
                .userId(updateReview.getUserId())
                .entityId(updateReview.getReviewId())
                .build());
        return updateReview;
    }

    public void removeReview(long reviewId) {
        Review removedReview = getReviewById(reviewId);
        reviewDao.removeReview(reviewId);
        feedService.addEvent(Event.builder()
                .eventType(EventType.REVIEW)
                .operation(EventOperation.REMOVE)
                .userId(removedReview.getUserId())
                .entityId(reviewId)
                .build());
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
