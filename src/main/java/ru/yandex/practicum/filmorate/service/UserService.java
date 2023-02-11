package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    final private UserStorage storage;

    private long nextId = 1;

    public User create(User user) {
        validate(user);
        user.setId(nextId++);
        storage.put(user);
        return user;
    }

    public User update(User user) {
        validate(user);
        storage.update(user);
        return user;
    }

    public List<User> getAll() {
        return storage.getAll();
    }
    public void addFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<Long> commonFriends(long userId, long friendId) {
        Set<Long> userFriends = storage.get(userId).getFriends();
        Set<Long> friendsFriends = storage.get(friendId).getFriends();
        return userFriends.stream().filter(friendsFriends::contains).collect(Collectors.toUnmodifiableList());
    }

    private void validate(@NonNull User user) {
        String exceptionText = "";

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            exceptionText = "Email " + user.getEmail() + " is not correct";
        }

        if (user.getLogin().isEmpty()) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Login mustn't be empty";
        }
        if (user.getLogin().contains(" ")) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "Login must have not space characters";
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            exceptionText += (exceptionText.isEmpty() ? "" : "\n")
                    + "User birthday cannot be later than current date";
        }

        if (!exceptionText.isEmpty()) {
            ValidationException exception = new ValidationException(exceptionText);
            log.warn("", exception);
            throw exception;
        }

        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }
}
