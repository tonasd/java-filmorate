package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>(); //key=userId

    @Override
    public User get(long userId) {
        if (!users.containsKey(userId)) {
            throw new ItemNotFoundException(String.format("User %d not found", userId));
        }
        return users.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User put(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if (users.replace(user.getId(), user) == null) {
            throw new ItemNotFoundException(String.format("User with id %d does not exist", user.getId()));
        }
    }

}
