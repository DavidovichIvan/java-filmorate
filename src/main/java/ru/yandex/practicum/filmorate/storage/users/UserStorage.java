package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> getUser(Integer id);

    List<User> getAllUsers();

    List<User> getFriends(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);

    boolean deleteUser(int userId);

    void deleteFriend(Integer userId, Integer friendId);

    User addUser(User user);

    User updateUser(User user);

    void addFriend(Integer userId, Integer newFriendId);
}