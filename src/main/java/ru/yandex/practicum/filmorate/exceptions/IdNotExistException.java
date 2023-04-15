package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class IdNotExistException extends RuntimeException {
    private String message;

    public IdNotExistException(Object o) {
        message = "";
        if (o instanceof User user) {
            this.message = "Не существует пользователя c id: " + user.getId();
        } else if (o instanceof Film film) {
            this.message = "Не найден фильм c id: " + film.getId();
        }
    }
}
