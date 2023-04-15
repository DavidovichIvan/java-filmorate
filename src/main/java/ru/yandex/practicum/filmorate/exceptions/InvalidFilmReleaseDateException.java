
package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class InvalidFilmReleaseDateException extends RuntimeException {
    final private String message;

    public InvalidFilmReleaseDateException() {
        this.message = "Дата релиза должна быть не ранее 28 декабря 1985 года.";
    }
}
