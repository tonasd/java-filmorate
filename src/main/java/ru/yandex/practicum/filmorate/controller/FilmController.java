package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController{
    private final FilmService service;

    @PostMapping
    public Film create(@RequestBody Film film) {
        service.create(film);
        log.info("Created film: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        service.update(film);
        log.info("Updated film: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        final List<Film> res = service.getAll();
        log.info("Get all films request. Given {} objects.", res.size());
        return res;
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable @Positive Long id) {
        final Film film = service.get(id);
        log.info("Given film: {}", film);
        return film;
    }

    @PutMapping(path = "/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") @Positive Long filmId, @PathVariable @Positive Long userId) {
        service.addLike(filmId, userId);
        log.info("Film {} is liked by user {}", filmId, userId);
    }

    @DeleteMapping(path = "/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") @Positive Long filmId, @PathVariable @Positive Long userId) {
        service.removeLike(filmId, userId);
        log.info("Film {} is unliked by user {}", filmId, userId);
    }

    @GetMapping(path = "/popular")
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10", value = "count")
                                     @Positive Integer size) {
        final List<Film> popularList = service.getPopular(size);
        log.info("Get {} popular films request. Given {} films", size, popularList.size());
        return popularList;
    }
}
