package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final FilmDao filmDao;
    private final FeedService feedService;

    public Review create(Review review) {
        validate(review);
        userDao.findUserById(review.getUserId());
        filmDao.findFilmById(review.getFilmId());
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
        validate(review);
        Review updatedReview = reviewDao.update(review);
        feedService.addEvent(Event.builder()
                .eventType(EventType.REVIEW)
                .operation(EventOperation.UPDATE)
                .userId(updatedReview.getUserId())
                .entityId(updatedReview.getReviewId())
                .build());
        return updatedReview;
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
        userDao.findUserById(userId);
        return reviewDao.putLike(reviewId, userId);
    }

    public Review putDislike(long reviewId, Long userId) {
        userDao.findUserById(userId);
        return reviewDao.putDislike(reviewId, userId);
    }

    public void removeLike(long reviewId, Long userId) {
        userDao.findUserById(userId);
        reviewDao.removeLike(reviewId, userId);
    }

    public void removeDislike(long reviewId, Long userId) {
        userDao.findUserById(userId);
        reviewDao.removeDislike(reviewId, userId);
    }

    protected void validate(Review review) {
        if (review.getContent() == null || review.getContent().isEmpty() || review.getContent().length() > 1024) {
            throw new ValidationException("Review content invalid");
        }
    }
}
