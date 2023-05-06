package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;
//класс-заготовка для статичных методов, которые могут использоваться как для фильмов так и для пользователей
public class VariablesValidation {

   public static int checkRequestId(String id) {
      try {
          return Integer.parseInt(id);
      } catch (NumberFormatException e) {
         throw new IdNotExistException();
      }
           }







}
