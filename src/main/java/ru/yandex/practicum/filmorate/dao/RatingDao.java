package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingDao {
    List<Rating> getAll();

    Rating get(int ratingId);
}
