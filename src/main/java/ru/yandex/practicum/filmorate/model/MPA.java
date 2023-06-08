package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;


@Data //геттер сеттер конструктор икуалс и хэшкод в одном флаконе
public class MPA {

    @NonNull
    private int id; //убрал что файнал

    //@NonNull  //@Nullable = это чтоб наоборот
    private String name; //под вопросом нужно ли тут имя хранить, так как сам рейтинг хранится в БД и извлекатеся по индексу

    public MPA(@NonNull int id, String name) {
        this.id = id;
        this.name = name;
    }
    //в тестах передают только id соответственно если тут оставлять это поле тогда при создании объекта нужно дополнительно обращатся к таблице с рейтингами и заполнять его
    //то есть если запрос на то чтобы получить рейтинг фильма - отправляется id вот отсюда из mpa в запросе

}
