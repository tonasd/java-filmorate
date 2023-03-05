package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendsDBStorage implements FriendsStorage{
    private final FriendsDao friendsDao;
    @Override
    public List<Long> getFriendsIds(long userId) {
        return friendsDao.getFriendsIds(userId);
    }

    @Override
    public void addFriend(long userIdRequestFrom, long friendIdRequestTo) {
        friendsDao.addFriend(userIdRequestFrom, friendIdRequestTo);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        friendsDao.removeFriend(userId, friendId);
    }
}
