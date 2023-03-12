package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/genres")
@RequiredArgsConstructor
public class GenresController {
    final private FilmService filmService;

    @GetMapping
    public List<Genre> getAll() {
        List<Genre> genres = filmService.getAllGenres();
        log.info("Returned list of {} genres", genres.size());
        return genres;
    }

    @GetMapping(value = "/{id}")
    public Genre get(@PathVariable int id) {
        Genre genre = filmService.getGenre(id);
        log.info("Found genre {}", genre);
        return genre;
    }
}
