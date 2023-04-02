package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface  EventDao {
    List<Event> getUserEvents(long id);

    void addEvent(Event event);
}