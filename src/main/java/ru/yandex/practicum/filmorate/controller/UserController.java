package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {
    @Override
    protected boolean isCorrect(User user) {
        String exceptionText = "";
        if (user.getLogin().isBlank()) {
            exceptionText = "Login must have not space characters";
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            exceptionText += exceptionText.isEmpty() ? "" : "\n"
                    + "User birthday cannot be later than current date";
        }

        if (!exceptionText.isEmpty()) {
            ValidationException exception = new ValidationException(exceptionText);
            log.warn("", exception);
            throw exception;
        }

        if (user.getName().isBlank()) user.setName(user.getLogin());
        return true;
    }

}
