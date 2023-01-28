package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public abstract class Item {
    @EqualsAndHashCode.Exclude
    protected int id;
    protected String name;

}
