package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserStorage storage;

    private long nextId = 1;

    public User create(User user) {
        validate(user);
        user.setId(nextId++);
        storage.put(user);
        return user;
    }

    public User update(User user) {
        validate(user);
        storage.update(user);
        return user;
    }

    public User get(long userId) {
        return storage.get(userId);
    }

    public List<User> getAll() {
        return storage.getAll();
    }
    public void addFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(long userId) {
        final User user = storage.get(userId);
        return user.getFriends().stream()
                .map(storage::get).collect(Collectors.toList());
    }

    public List<User> commonFriends(long userId, long friendId) {
        Set<Long> userFriends = storage.get(userId).getFriends();
        Set<Long> friendsFriends = storage.get(friendId).getFriends();
        return userFriends.stream()
                .filter(friendsFriends::contains)
                .map(storage::get)
                .collect(Collectors.toUnmodifiableList());
    }

    public void validate( User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
