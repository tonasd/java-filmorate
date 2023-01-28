package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@RequestMapping("/users")
public class UserController extends Controller<User>{
@Override
protected boolean isCorrect(User user) {
        if (user == null) throw new ValidationException("User object cannot be null");
        if (user.getLogin().isBlank()) throw new ValidationException("Login must have not space characters");
        if (user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday cannot be later than current date");
        }

        return true;
    }

}
