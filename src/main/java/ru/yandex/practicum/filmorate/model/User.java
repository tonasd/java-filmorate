package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends Item {
    @NotEmpty
    @Email(message = "'${validatedValue}' is incorrect address")
    String email;
    @NotEmpty(message = "mustn't be empty")
    @Pattern(regexp = "\\S+", message = "Login must have not space characters")
    String login;
    String name;
    @PastOrPresent(message = "cannot be later than current date")
    LocalDate birthday;
    @ToString.Exclude
    private transient Set<Long> friends = new HashSet<>();

    public User(User user) {
        this(user.email, user.login, user.name, user.birthday, new HashSet<>(user.friends));
        this.id = user.id;
    }

    @ToString.Include(name = "friendsQuantity")
    private String friends() {
        return String.valueOf(friends.size());
    }
}
