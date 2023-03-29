package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FriendsDao {
    void addFriend(long userIdRequestFrom, long friendIdRequestTo);

    void removeFriend(long userId, long friendId);

    List<Long> getFriendsIds(long userId);
}