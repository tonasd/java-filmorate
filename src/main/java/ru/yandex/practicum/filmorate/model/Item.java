package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public abstract class Item {
    protected int id;
    protected String name;

}
