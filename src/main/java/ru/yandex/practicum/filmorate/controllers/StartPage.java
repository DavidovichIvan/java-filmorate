package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StartPage {

    @GetMapping("/home")
    public String homePage() {
        final String startMessage = "Добро пожаловать, в приложение Filmorate!";
        log.info("Запрошена стартовая страница");

        return startMessage;
    }

}
