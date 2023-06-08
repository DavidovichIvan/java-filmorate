package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorageDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@Service
public class UserServiceDB {

    private final UserStorageDB userStorage;

    @Autowired
    public UserServiceDB(UserStorageDB userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> getUser(String id) {
        return userStorage.getUser(Integer.valueOf(id));
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> getFriends(String id) {
        return userStorage.getFriends(Integer.valueOf(id));
    }

    public List<User> getCommonFriends(String id, String otherId) {
        int firstUserId = Integer.parseInt(id);
        int secondUserId = Integer.parseInt(otherId);
        return userStorage.getCommonFriends(firstUserId, secondUserId);
    }

    public boolean deleteUser(String id) {
       Integer userId = Integer.valueOf(id);
        return userStorage.deleteUser(userId);
           }

    public void deleteFriend(String id, String friendId) {
        int userId = Integer.parseInt(id);
        int friendToDeleteId = Integer.parseInt(friendId);

        userStorage.deleteFriend(userId, friendToDeleteId);
    }

    public void addFriend(String id, String friendId) {
        int userId = Integer.parseInt(id);
        int newFriendId = Integer.parseInt(friendId);

        userStorage.addFriend(userId, newFriendId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

}
