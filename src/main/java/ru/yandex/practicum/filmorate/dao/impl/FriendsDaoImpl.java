package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public class FriendsDaoImpl implements FriendsDao {
    @Override
    public void addFriend(long userIdRequestFrom, long friendIdRequestTo) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public List<User> getFriends(long userId) {
        return null;
    }

    @Override
    public List<User> commonFriends(long userId, long friendId) {
        return null;
    }
}
