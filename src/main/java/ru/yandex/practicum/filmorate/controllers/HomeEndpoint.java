package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeEndpoint {

    @GetMapping("/home")
    public String home() {
        final String defaultMessage = "приложение Filmorate";
        log.info("Запрос к эндпоинту /home");

        return defaultMessage;
    }
}
