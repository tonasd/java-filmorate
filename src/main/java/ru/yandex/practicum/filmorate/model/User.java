package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
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

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this(email, login, name, birthday);
        this.id = id;
    }
}