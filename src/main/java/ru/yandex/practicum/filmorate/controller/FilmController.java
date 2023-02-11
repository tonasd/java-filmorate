package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
    protected List<Film> getAll() {
        final List<Film> res = service.getAll();
        log.info("Get all films request. Given {} objects.", res.size());
        return res;
    }
}
