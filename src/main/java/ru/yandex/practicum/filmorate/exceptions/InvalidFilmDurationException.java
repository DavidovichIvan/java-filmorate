
package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class InvalidFilmDurationException extends RuntimeException {

    private final String message;

    public InvalidFilmDurationException() {

        this.message = "Продолжительность фильма должна быть указана в минутах больше нуля.";
    }

}

