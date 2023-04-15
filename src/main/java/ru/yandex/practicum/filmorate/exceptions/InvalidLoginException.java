package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class InvalidLoginException extends RuntimeException {
    private final String message;

    public InvalidLoginException() {
        this.message = "Логин не может быть пустым и содержать пробелы";
    }

}
