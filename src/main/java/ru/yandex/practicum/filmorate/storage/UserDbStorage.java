package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage{
    private final UserDao userDao;
    @Override
    public User get(long userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAllUsers();
    }

    @Override
    public User put(User user) {
        return userDao.addUser(user);
    }

    @Override
    public void update(User user) {
        userDao.updateUser(user);
    }
}
