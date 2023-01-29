package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
abstract class Controller <T extends Item> {

    private final Set<T> items = new HashSet<>();
    private int nextId = 1;

    @PostMapping
    protected T create(@RequestBody T item) {
        log.info("Post {} request", item.getClass().getSimpleName());
        if (isCorrect(item)) {
            item.setId(nextId++);
            log.debug("Created: {}", item);
            items.add(item);
        }
        return item;

    }

    @PutMapping
    protected T update(@RequestBody T item) {
        log.info("Put {} request", item.getClass().getSimpleName());
        if (isCorrect(item)) {
            log.debug("Updated: {}", item);
            items.add(item);
        }
        return item;
    }

    @GetMapping
    protected List<T> getAll() {
        log.info("Get all {} request", this.getClass().getGenericSuperclass());
        return List.copyOf(items);
    }

    abstract protected boolean isCorrect(T item);
}

