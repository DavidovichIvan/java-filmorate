package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class IdAlreadyExistsException extends RuntimeException {
    private String message;

    public IdAlreadyExistsException(Object o) {
       this.message = "Такой id уже существует!";
            }
}
