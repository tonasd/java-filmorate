package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody @Valid Review review) {
        review = reviewService.create(review);
        log.info("Created review: {}", review);
        return review;
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        review = reviewService.update(review);
        log.info("Updated review: {}", review);
        return review;
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable(name = "id") long reviewId) {
        reviewService.removeReview(reviewId);
        log.info("Deleted review with id {}", reviewId);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable(name = "id") @Valid long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        log.info("Found review: {}", review);
        return review;
    }

    @GetMapping
    public List<Review> getReviewsByFilmId(@RequestParam(defaultValue = "0") long filmId,
                                           @RequestParam(defaultValue = "10") int count) {
        final List<Review> reviews = reviewService.getReviewsByFilmId(filmId, count);
        log.info("Found {} reviews of film with id {}", reviews.size(), filmId);
        return reviews;
    }

    @PutMapping("/{id}/like/{userId}")
    public Review putLike(@PathVariable("id") long reviewId,
                          @PathVariable("userId") long userId) {
        final Review review = reviewService.putLike(reviewId, userId);
        log.info("User with id {} put like to review with id {}", userId, reviewId);
        return review;
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review putDislike(@PathVariable("id") long reviewId,
                             @PathVariable("userId") long userId) {
        final Review review = reviewService.putDislike(reviewId, userId);
        log.info("User with id {} put dislike to review with id {}", userId, reviewId);
        return review;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long reviewId,
                           @PathVariable("userId") long userId) {
        reviewService.removeLike(reviewId, userId);
        log.info("User with id {} remove like from review with id {}", userId, reviewId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable("id") int reviewId,
                              @PathVariable("userId") long userId) {
        reviewService.removeDislike(reviewId, userId);
        log.info("User with id {} remove dislike from review with id {}", userId, reviewId);
    }
}
