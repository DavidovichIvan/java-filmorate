package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataBaseNotFoundException extends RuntimeException {

        private final String message;

        public DataBaseNotFoundException() {
            this.message = "Пользователь/фильм не найден.";
        }

    public DataBaseNotFoundException(String message) {

        this.message = message;
    }


    }





