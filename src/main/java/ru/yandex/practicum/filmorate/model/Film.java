package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@Builder(toBuilder = true)
public class Film extends Item {
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
