package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBirthdayException extends RuntimeException {
    private final String message;

    public InvalidBirthdayException(LocalDate date) {
        this.message = "Дата рождения не может быть в будущем -" + date;
    }
}
