package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/directors")
@RequiredArgsConstructor
public class DirectorsController {
    private final DirectorService directorService;

    @PostMapping
    public Director create(@RequestBody @Valid Director director) {
        director = directorService.create(director);
        log.info("Created director {}", director);
        return director;
    }

    @PutMapping
    public Director update(@RequestBody @Valid Director director) {
        director = directorService.update(director);
        log.info("Updated director {}", director);
        return director;
    }

    @GetMapping
    public List<Director> getAll() {
        List<Director> directors = directorService.getAll();
        log.info("Returned list of {} directors", directors.size());
        return directors;
    }

    @GetMapping(value = "/{id}")
    public Director get(@PathVariable Long id) {
        Director director = directorService.get(id);
        log.info("Found director {}", director);
        return director;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
        log.info("Deleted the director with id={}", id);
    }
}
