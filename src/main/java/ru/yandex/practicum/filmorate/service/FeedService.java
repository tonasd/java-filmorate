package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final EventDao eventDao;
    private final UserDao userDao;

    public List<Event> getByUserId(long id) {
        userDao.findUserById(id); // Checking user
        return eventDao.getUserEvents(id);
    }

    public void addEvent(Event event) {
        eventDao.addEvent(event);
    }
}