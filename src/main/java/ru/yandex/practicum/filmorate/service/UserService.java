package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final FriendsDao friendsDao;

    public User create(User user) {
        validate(user);
        return userDao.addUser(user);
    }

    public User update(User user) {
        validate(user);
        userDao.updateUser(user);
        return userDao.findUserById(user.getId());
    }

    public User get(long userId) {
        return userDao.findUserById(userId);
    }

    public List<User> getAll() {
        return userDao.getAllUsers();
    }

    public void addFriend(long userId, long friendId) {
        friendsDao.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        friendsDao.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        return friendsDao.getFriendsIds(userId).stream()
                .map(userDao::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(long userId, long friendId) {
        List<Long> userFriendsIds = friendsDao.getFriendsIds(userId);
        List<Long> friendsFriendsIds = friendsDao.getFriendsIds(friendId);
        userFriendsIds.retainAll(friendsFriendsIds);
        return userFriendsIds.stream()
                .map(userDao::findUserById)
                .collect(Collectors.toList());
    }

    public void deleteUserById(Long userId) {
        userDao.deleteUserById(userId);
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}