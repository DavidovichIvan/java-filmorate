
package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class InvalidFilmNameException extends RuntimeException {
    private final String message;

    public InvalidFilmNameException() {

        this.message = "Не указано название фильма";
    }
}
