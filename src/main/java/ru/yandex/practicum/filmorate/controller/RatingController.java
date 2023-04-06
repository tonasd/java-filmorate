package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Rating get(@PathVariable(value = "id") int ratingId) {
        Rating rating = filmService.getRating(ratingId);
        log.info("Found rating {}", rating);
        return rating;
    }

    @GetMapping
    public List<Rating> getAll() {
        List<Rating> ratings = filmService.getAllRatings();
        log.info("Returned list of {} ratings", ratings.size());
        return ratings;
    }
}