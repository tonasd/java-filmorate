package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    private final int id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
