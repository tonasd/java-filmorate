package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    void addFriend(long userIdRequestFrom, long friendIdRequestTo);
    void removeFriend(long userId, long friendId);
    List<User> getFriends(long userId);
    List<User> commonFriends(long userId, long friendId);
}
