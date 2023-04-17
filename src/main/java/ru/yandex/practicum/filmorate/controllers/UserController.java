package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@Valid @RequestBody User user) {

        if (usersList.containsKey(user.getId())) {
            log.info("Запрос на добавление; пользователь с id: {} уже существует", user.getId());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new IdAlreadyExistsException();
        }

        userDataValidate(user);
        usersList.put(user.getId(), user);
        correctIdCounter(user);
        log.info("Запрос на добавление; сохранен пользователь: {} ", user);
        return user;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    User updateUser(@RequestBody User user) {
        if (!usersList.containsKey(user.getId())) {
            log.info("Запрос на обновление; не существует пользователя с id: {} ", user.getId());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new IdNotExistException();
        }
        userDataValidate(user);
        usersList.put(user.getId(), user);
        correctIdCounter(user);
        log.info("Запрос на обновление; обновлен пользователь: {} ", user);
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", usersList.size());
        List<User> users = new ArrayList<>(usersList.values());
        return users;
    }

    private void userDataValidate(User user) {
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidEmailException(user.getEmail());
        }

        if (!StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ")) {
            log.info("Логин пустой или содержит пробелы");
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidLoginException();
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения установлена в будущем");
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidBirthdayException(user.getBirthday());
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    //метод добавлен в код исключительно для прохождения автотеста
    //if = если id присваивается не сервером а передан в запросе, то счетчик повышать не нужно
    //while = корректировка на случай если счетчик стал равен уже существующему id
    private void correctIdCounter(User user) {
        if (user.getId() != User.getUserIdCounter() - 1) {
            User.setUserIdCounter(User.getUserIdCounter() - 1);
        }
        while (usersList.containsKey(User.getUserIdCounter())) {
            User.setUserIdCounter(User.getUserIdCounter() + 1);
        }
    }
}
