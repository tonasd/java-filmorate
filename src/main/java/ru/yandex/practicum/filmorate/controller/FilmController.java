package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    @Override
    protected boolean isCorrect(@NonNull Film film) {
        String exceptionText = "";
        if (film.getName().isBlank()) {
            exceptionText += "Film's name cannot be empty";
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Film description should be not longer than 200 characters";
        }
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Date of the film release is incorrect(before the very first screening)";
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Duration of a film must be positive";
        }

        if (!exceptionText.isEmpty()) {
            ValidationException exception = new ValidationException(exceptionText);
            log.warn("", exception);
            throw exception;
        }

        return true;
    }
}
