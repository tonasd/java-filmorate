package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected Map<String, String> handleNotFound(final ItemNotFoundException e) {
        log.warn(e.toString());
        return Map.of("Could not find", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn(e.toString());
        return Map.of(e.getFieldError().getField(), e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected Map<String, String> handleNotValidated(final ValidationException e) {
        log.warn(e.toString());
        return Map.of("Validation error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Map<String, String> handleOtherErrors(final Throwable e) {
        log.warn(e.toString());
        return Map.of("Unknown error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected Map<String, String> handleNotFound(final ReviewNotFoundException e) {
        log.warn(e.toString());
        return Map.of("Could not find", e.getMessage());
    }
}
