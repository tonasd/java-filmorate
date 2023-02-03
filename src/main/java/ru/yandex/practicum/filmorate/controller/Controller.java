package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Item;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
abstract class Controller <T extends Item> {
    private final Map<Long,T> items;
    private long nextId;
    private final String itemTypeName; // keeps name of model in items, for logging

    Controller() {
        items = new HashMap<>();
        this.nextId = 1L;
        //saving T simpleName
        final Type type =((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.itemTypeName = type.getTypeName().substring(type.getTypeName().lastIndexOf('.') + 1);
    }

    @PostMapping
    protected T create(@RequestBody T item) {
        if (isCorrect(item)) {
            item.setId(nextId++);
            log.info("Created {}: {}", itemTypeName, item);
            items.put(item.getId(), item);
        }
        return item;
    }

    @PutMapping
    protected T update(@RequestBody T item) {
        if (isCorrect(item) && items.containsKey(item.getId())) {
            log.info("Updated {}: {}", itemTypeName, item);
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
        final List<T> res = List.copyOf(items.values());
        log.info("Get all {}s request. Given {} objects.", itemTypeName, res.size());
        return res;
    }

    abstract protected boolean isCorrect(T item);
}

