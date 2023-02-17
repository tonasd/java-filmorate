package ru.yandex.practicum.filmorate.model;

import lombok.*;

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
public class User extends Item {
    @NotEmpty
    @Email(message = "'${validatedValue}' is incorrect address")
    private String email;
    @NotEmpty(message = "mustn't be empty")
    @Pattern(regexp = "\\S+", message = "Login must have not space characters")
    private String login;
    private String name;
    @PastOrPresent(message = "cannot be later than current date")
    private LocalDate birthday;
    @ToString.Exclude
    private transient Set<Long> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this(email, login, name, birthday, new HashSet<>());
    }

    public User(User user) {
        this(user.email, user.login, user.name, user.birthday, user.friends);
        this.id = user.id;
    }

    @ToString.Include(name = "friendsQuantity")
    private String friends() {
        return String.valueOf(friends.size());
    }
}
