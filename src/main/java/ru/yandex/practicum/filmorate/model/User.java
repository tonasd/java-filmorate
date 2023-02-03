package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
public class User extends Item {
    @NonNull
    @EqualsAndHashCode.Include
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;
}
