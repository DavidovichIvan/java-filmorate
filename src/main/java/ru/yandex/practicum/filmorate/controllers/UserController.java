package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    Map<Integer, User> usersList = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        if (usersList.containsKey(user.getId())) {
            log.info("Запрос на добавление; пользователь с id: {} уже существует", user.getId());
            throw new IdAlreadyExistsException(user);
        }

        userDataValidate(user);
        usersList.put(user.getId(), user);
        log.info("Запрос на добавление; сохранен пользователь: {} ", user);
        return user;
    }

    @PutMapping
    User updateUser(@RequestBody User user) {
        if (!usersList.containsKey(user.getId())) {
            log.info("Запрос на обновление; не существует пользователя с id: {} ", user.getId());
            throw new IdNotExistException(user);
        }
        userDataValidate(user);

        usersList.put(user.getId(), user);
        log.info("Запрос на обновление; обновлен пользователь: {} ", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", usersList.size());
        List<User> users = new ArrayList<>(usersList.values());
        return users;
    }


    private void userDataValidate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            throw new InvalidEmailException(user.getEmail());
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Логин пустой или содержит пробелы");
            throw new InvalidLoginException();
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения установлена в будущем");
            throw new InvalidBirthdayException(user.getBirthday());
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
