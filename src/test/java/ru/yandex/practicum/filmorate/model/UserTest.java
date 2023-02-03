package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {


    @Test
    public void shouldThrowNullPointerExceptionForEmail() {
        assertThrows(NullPointerException.class, () -> new User(null, "login", "name", LocalDate.EPOCH));
    }

    @Test
    public void shouldThrowNullPointerExceptionForLogin() {
        assertThrows(NullPointerException.class, () -> new User("a@a.ru", null, "name", LocalDate.EPOCH));
    }
}