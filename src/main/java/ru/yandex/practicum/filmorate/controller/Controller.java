package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
abstract class Controller <T extends Item> {

    private final Set<T> items = new HashSet<>();
    private int nextId = 1;

    @PostMapping
    public T create(@NonNull @RequestBody T item) {
        log.info("Post {} request", item.getClass().getSimpleName());
        if (isCorrect(item)) {
            item.setId(nextId++);
            log.debug("Created:", item);
            items.add(item);
        }
        return item;

    }

    @PutMapping
    public T update(@NonNull @RequestBody T item) {
        log.info("Put {} request", item.getClass().getSimpleName());
        if (isCorrect(item)) {
            log.debug("Updated:", item);
            items.add(item);
        }
        return item;
    }

    @GetMapping
    public List<T> getAll() {
        log.info("Get all {} request", this.getClass().getGenericSuperclass());
        return List.copyOf(items);
    }

    abstract protected boolean isCorrect(T item);
}

