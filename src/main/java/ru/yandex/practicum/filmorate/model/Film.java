package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = true)
@Builder(toBuilder = true)
public class Film extends Item {
    @NonNull @EqualsAndHashCode.Include
    final private String name;
    private String description;
    @EqualsAndHashCode.Include
    private LocalDate releaseDate;
    private int duration;
}
