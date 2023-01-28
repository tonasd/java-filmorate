package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
abstract class Controller <T extends Item> {

    private final Set<T> items = new HashSet<>();
    private int nextId = 1;

    @PostMapping
    public T create(@NonNull @RequestBody T item) {
        if (isCorrect(item)) {
            item.setId(nextId++);
            items.add(item);
        }
        return item;

    }

    @PutMapping
    public T update(@NonNull @RequestBody T item) {
        if (isCorrect(item)) {
            items.add(item);
        }
        return item;
    }

    @GetMapping
    public List<T> getAll() {
        return List.copyOf(items);
    }

    abstract protected boolean isCorrect(T item);
}

