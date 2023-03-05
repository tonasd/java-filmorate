package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User get(long userId);
    List<User> getAll();
    User put(User user);
    void update(User user);
}
