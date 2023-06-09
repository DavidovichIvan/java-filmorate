
package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataBaseException extends RuntimeException {

    private final String message;

    public DataBaseException() {
        this.message = "Ошибка работы с базой данных.";
    }

    public DataBaseException(String message) {
        this.message = message;
    }

}
