package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendsStorage {
    List<Long> getFriendsIds(long userId);
    void addFriend(long userIdRequestFrom, long friendIdRequestTo);
    void removeFriend(long userId, long friendId);
}
