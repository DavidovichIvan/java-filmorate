package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    Map<Integer, User> getUsersList();

    User getUser(Integer id);

    void addFriend(Integer userId, Integer newFriendId);

    void deleteFriend(Integer userId, Integer friendId);

   List<User> getFriends(Integer id);

   List<User> getCommonFriends(Integer id, Integer otherId);

}