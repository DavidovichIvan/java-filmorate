package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class InvalidEmailException extends RuntimeException {

    final private String message;

    public InvalidEmailException(String email) {

        this.message = "Введен неверный адрес электронной почты: " + email +
                " Электронная почта не может быть пустой и должна содержать символ @";

    }
}
