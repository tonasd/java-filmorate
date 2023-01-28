package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends Item {
    @NonNull @EqualsAndHashCode.Include
    private final String email;
    @NonNull
    private final String login;
    private final LocalDate birthday;

}
