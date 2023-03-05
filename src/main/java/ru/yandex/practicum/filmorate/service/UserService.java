package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserStorage userStorage;
    final private FriendsStorage friendsStorage;

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
        friendsStorage.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        friendsStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        final User user = userStorage.get(userId);
        return friendsStorage.getFriendsIds(userId).stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(long userId, long friendId) {
        List<Long> userFriendsIds = friendsStorage.getFriendsIds(userId);
        List<Long> friendsFriendsIds = friendsStorage.getFriendsIds(friendId);
        userFriendsIds.retainAll(friendsFriendsIds);
        return userFriendsIds.stream().
                map(userStorage::get)
                .collect(Collectors.toList());
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
