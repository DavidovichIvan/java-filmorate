package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;

//класс-заготовка
public class VariablesValidation {

    public static int checkRequestId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IdNotExistException();
        }
    }
}