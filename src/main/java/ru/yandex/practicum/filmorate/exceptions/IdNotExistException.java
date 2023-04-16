package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdNotExistException extends RuntimeException {
    private String message;

    public IdNotExistException(Object o) {
                   this.message = "Не существует пользователя/фильма таким c id!";

    }
}
