
package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDescriptionException extends RuntimeException {

    private final String message;

    public InvalidDescriptionException(int maxLength) {
        this.message = ("Описание фильма не должно превышать " + maxLength + " символов");

    }

}
