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
    final private UserStorage userStorage;

    public User create(User user) {
        validate(user);
        return userStorage.put(user);
    }

    public User update(User user) {
        validate(user);
        userStorage.update(user);
        return userStorage.get(user.getId());
    }

    public User get(long userId) {
        return userStorage.get(userId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }
    public void addFriend(long userId, long friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(long userId) {
        final User user = userStorage.get(userId);
        return user.getFriends().stream()
                .map(userStorage::get).collect(Collectors.toList());
    }

    public List<User> commonFriends(long userId, long friendId) {
        Set<Long> userFriends = userStorage.get(userId).getFriends();
        Set<Long> friendsFriends = userStorage.get(friendId).getFriends();
        return userFriends.stream()
                .filter(friendsFriends::contains)
                .map(userStorage::get)
                .collect(Collectors.toUnmodifiableList());
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
