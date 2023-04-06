package ru.yandex.practicum.filmorate.exception;

public class WrongRequestParameterException extends RuntimeException {
    public WrongRequestParameterException(String description) {
        super(description);
    }
}
