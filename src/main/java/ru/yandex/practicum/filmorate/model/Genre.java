package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Data //геттер сеттер конструктор икуалс и хэшкод в одном флаконе
public class Genre {

    @NonNull
    @Getter
    @Setter
    private int id; //убрал что файнал
    private String name;

    public Genre(@NonNull int id, String name) {
        this.id = id;
        this.name = name;
    }
}
