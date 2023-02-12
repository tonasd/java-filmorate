package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.constraints.Positive;
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
    public List<User> getAll() {
        final List<User> res = service.getAll();
        log.info("Get all users request. Given {} objects.", res.size());
        return res;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable @Positive Long id) {
        final User user = service.get(id);
        log.info("Given user: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        service.addFriend(id, friendId);
        log.info("Users {} and {} now friends", id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        service.removeFriend(id, friendId);
        log.info("Users {} and {} not friends anymore", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive Long id) {
        final List<User> friends = service.getFriends(id);
        log.info("Returned list of {} friends for user {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Positive Long id, @PathVariable @Positive Long otherId) {
        final List<User> commonFriends = service.commonFriends(id, otherId);
        log.info("Returned list of {} common friends for users {} and {}", commonFriends.size(), id, otherId);
        return commonFriends;
    }



}
