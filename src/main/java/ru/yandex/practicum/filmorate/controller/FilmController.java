package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@RequestMapping("/films")
public class FilmController extends Controller<Film>{

@Override
protected boolean isCorrect(Film film) {
        if (film.getName().isBlank()) throw new ValidationException("Film's name cannot be empty");
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Film description should be not longer than 200 characters");
        }
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Date of the film release is incorrect(before the very first screening)");
        }
        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Duration of a film must be positive");
        }

        return true;
    }
}
