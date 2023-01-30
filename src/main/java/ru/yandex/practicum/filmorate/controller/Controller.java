package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
abstract class Controller <T extends Item> {

    private final Map<Integer,T> items = new HashMap<>();
    private int nextInt = 1;

    @PostMapping
    protected T create(@RequestBody T item) {
        log.info("Post {} request", item.getClass().getSimpleName());
        if (isCorrect(item)) {
            item.setId(nextInt);
            log.debug("Created: {}", item);
            items.put(nextInt++, item);
        }
        return item;

    }

    @PutMapping
    protected T update(@RequestBody T item) {
        log.info("Put {} request", item.getClass().getSimpleName());
        isCorrect(item);
        if (items.containsKey(item.getId())) {
            log.debug("Updated: {}", item);
            items.replace(item.getId(), item);
        } else {

            ValidationException exception = new ValidationException(String.format("%s with id %s doesn't exist",
                    item.getClass().getSimpleName(), item.getId()));
            log.warn("", exception);
            throw exception;
        }

        return item;
    }

    @GetMapping
    protected List<T> getAll() {
        log.info("Get all {} request", this.getClass().getGenericSuperclass());
        return List.copyOf(items.values());
    }

    abstract protected boolean isCorrect(T item);
}

