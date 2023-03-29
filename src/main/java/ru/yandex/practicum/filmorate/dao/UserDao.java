package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    User findUserById(long id); // get user from DB

    List<User> getAllUsers();

    User addUser(User user);

    void updateUser(User user);

    void deleteUserById(long id);
}