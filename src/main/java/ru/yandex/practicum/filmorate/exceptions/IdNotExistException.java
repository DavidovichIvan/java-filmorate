package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNotExistException extends RuntimeException {
    private String message;

    public IdNotExistException() {
                   this.message = "Не существует пользователя/фильма таким c id либо параметр запроса передан в нечисловом формате!";

    }
}
