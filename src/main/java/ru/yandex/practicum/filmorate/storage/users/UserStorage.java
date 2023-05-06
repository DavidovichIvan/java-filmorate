package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

//@Component //это интерфейс; сделал сам класс Component, тут убрал
public interface UserStorage {

    User addUser(User user);
    User updateUser(User user);
    List<User> getAllUsers();
    Map<Integer, User> getUsersList();

}
