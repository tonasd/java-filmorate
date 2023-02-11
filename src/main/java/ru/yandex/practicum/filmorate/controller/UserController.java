package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController{
    private final UserService service;

    @PostMapping
    public User create(@RequestBody User user) {
        service.create(user);
        log.info("Created user: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        service.update(user);
        log.info("Updated user: {}", user);
        return user;
    }

    @GetMapping
    protected List<User> getAll() {
        final List<User> res = service.getAll();
        log.info("Get all users request. Given {} objects.", res.size());
        return res;
    }



}
