package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
public class User extends Item {
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;

    public User(@NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        this(email, login, name, birthday, new HashSet<>());
    }

    public User(User user) {
        this(user.email, user.login, user.name, user.birthday, user.friends);
        this.id = user.id;
    }

    @ToString.Exclude
    @Singular("addFriendId") private Set<Long> friends;

    @ToString.Include(name = "friendsQuantity")
    private String friends() {
        return String.valueOf(friends.size());
    }
}
