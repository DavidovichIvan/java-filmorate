package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@Service
public class UserServiceDB {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceDB(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> getUser(int id) {
        return userStorage.getUser(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public boolean deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
}