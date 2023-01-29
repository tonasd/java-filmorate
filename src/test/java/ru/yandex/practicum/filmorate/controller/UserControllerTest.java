package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void create() {
        User expected = new User("user@mail.ru", "login", "Aksinya", LocalDate.of(1990, 4, 1));
        controller.create(expected);
        User actual = controller.getAll().get(0);
        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertNotEquals(0, actual.getId());
    }

    @Test
    void isCorrect() {
        User user = new User("user@mail.ru", "login", "", LocalDate.of(1990, 4, 1));
        assertTrue(controller.isCorrect(user));
        assertEquals("login", user.getName());

        User userWithWrongEmail = user.toBuilder().email("without at").build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(userWithWrongEmail));
        User userWithWrongEmail2 = user.toBuilder().email("").build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(userWithWrongEmail2));

        User userWithEmptyLogin = user.toBuilder().login("").build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(userWithEmptyLogin));
        User userWithSpaceInLogin = user.toBuilder().login("2+2= 4").build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(userWithSpaceInLogin));
        user.setName("");
        controller.isCorrect(user);
        assertEquals("login", user.getName());

        User userWithBirthdayInFuture = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        assertThrows(ValidationException.class, ()-> controller.isCorrect(userWithBirthdayInFuture));
        User userWithBirthdayToday = user.toBuilder().birthday(LocalDate.now()).build();
        assertDoesNotThrow(()-> controller.isCorrect(userWithBirthdayToday));
    }
}