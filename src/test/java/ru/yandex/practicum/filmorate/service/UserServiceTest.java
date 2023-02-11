package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static UserService service;
    static User user;
    static User user2;

    @BeforeAll
    static void setUp() {
        service = new UserService(new InMemoryUserStorage());
        user = new User("user@mail.ru", "login", "Aksinya", LocalDate.of(1990, 4, 1));
        user2 = new User("mavra@mail.net", "mavralogin", "Mavra", LocalDate.of(1990, 2, 11));

    }

    @Test
    void create() {
        service.create(user);
        User actual = service.getAll().get(0);
        assertEquals(user, actual);
        assertNotEquals(0, actual.getId());

        User userWithWrongEmail = new User(user);
        userWithWrongEmail.setEmail("without at");
        assertThrows(ValidationException.class, () -> service.create(userWithWrongEmail));
        userWithWrongEmail.setEmail("");
        assertThrows(ValidationException.class, () -> service.create(userWithWrongEmail));

        User userWrongLogin = new User(user);
        userWrongLogin.setLogin("");
        assertThrows(ValidationException.class, () -> service.create(userWrongLogin));
        userWrongLogin.setLogin("2+2= 4");
        assertThrows(ValidationException.class, () -> service.create(userWrongLogin));

        User userWrongBirthday = new User(user);
        final LocalDate dateInFuture = LocalDate.now().plusDays(1);
        userWrongBirthday.setBirthday(dateInFuture);
        assertThrows(ValidationException.class, () -> service.create(userWrongBirthday));
    }

    @Test
    void getAll() {
        service.create(user2);
        int expectedSize = 2;
        List<User> all = service.getAll();
        assertEquals(expectedSize, all.size());
        assertTrue(all.contains(user));
        assertTrue(all.contains(user2));
    }

    @Test
    void update() {
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> service.update(user));

        User beforeUpdate = service.getAll().get(0);
        User afterUpdate = new User(beforeUpdate);
        afterUpdate.setName("Stepan");
        user = service.update(afterUpdate);
        assertNotEquals(beforeUpdate, afterUpdate);
        assertEquals(afterUpdate,
                service.getAll().stream().filter(user -> afterUpdate.getId() == user.getId()).findFirst().get());
    }

    @Test
    void addFriend() {
        service.addFriend(user.getId(), user2.getId());
        assertThrows(ItemNotFoundException.class, () -> service.addFriend(user.getId(), 9999));
        assertEquals(1, user.getFriends().size());
        assertTrue(user.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user.getId()));
    }
    @Test
    void removeFriend() {
        service.removeFriend(user.getId(), user2.getId());
        assertTrue(user2.getFriends().isEmpty());
        assertTrue(user.getFriends().isEmpty());
    }

    @Test
    void commonFriends() {
        service.addFriend(user.getId(), user2.getId());
        User user3 = new User(user);
        service.create(user3);
        service.addFriend(user3.getId(), user.getId());
        assertTrue(user3.getFriends().containsAll(user2.getFriends()));
    }




}