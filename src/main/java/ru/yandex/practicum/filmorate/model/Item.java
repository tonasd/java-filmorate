package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class Item {
    @EqualsAndHashCode.Exclude
    protected long id;
}
