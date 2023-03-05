package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    public User(User user) {
        this(user.email, user.login, user.name, user.birthday);
        this.id = user.id;
    }

    public User(long id,String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", this.email);
        values.put("login", this.login);
        values.put("name", this.name);
        values.put("birthday", this.birthday);
        if(this.id != 0) {
            values.put("user_id", this.id);
        }
        return values;
    }
}
