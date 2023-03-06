package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating extends Item {
    @NotNull @Size(max = 32, min = 1)
    String name;
    @NotNull @Size(max = 255, min = 1)
    String description;
}
