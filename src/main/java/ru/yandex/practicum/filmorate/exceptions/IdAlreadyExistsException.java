package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class IdAlreadyExistsException extends RuntimeException {
    private String message;
    
    public IdAlreadyExistsException(Object o) {
        
        message = "";
        if (o instanceof User user) {
            this.message = "Пользователь с таким id {} уже существует!: " + user.getId();
        }
        if (o instanceof Film film) {
            this.message = "Фильм с таким id {} уже существует!: " + film.getId();
        }
    }
}
