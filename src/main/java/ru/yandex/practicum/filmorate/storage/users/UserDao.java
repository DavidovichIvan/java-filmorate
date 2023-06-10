package ru.yandex.practicum.filmorate.storage.users;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataBaseException;
import ru.yandex.practicum.filmorate.exceptions.DataBaseNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Setter
@Component
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<User> getUser(Integer id) {
        return getSingleUserRecord(id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try {
            SqlRowSet usersId = jdbcTemplate.queryForRowSet("select * from Users_DB");
            while (usersId.next()) {
                Integer userId = usersId.getInt("user_id");
                Optional<User> optUser = getSingleUserRecord(userId);
                allUsers.add(optUser.get());
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return allUsers;
    }

    @Override
    public List<User> getFriends(Integer id) {
        List<User> allFriends = new ArrayList<>();
        try {
            SqlRowSet friendsId = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?", id);
            while (friendsId.next()) {
                Integer friendId = friendsId.getInt("friend_id");
                Optional<User> optFriend = getSingleUserRecord(friendId);
                allFriends.add(optFriend.get());
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return allFriends;
    }

    @Override
    public List<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        List<User> commonFriendsList = new ArrayList<>();

        try {
            SqlRowSet commonFriends = jdbcTemplate.queryForRowSet("SELECT f1.FRIEND_ID \n" +
                    "FROM friends f1 INNER JOIN friends f2\n" +
                    "  ON f1.friend_id = f2.friend_id\n" +
                    "WHERE f1.user_id = ?\n" +
                    "  AND f2.user_id = ?", firstUserId, secondUserId);
            while (commonFriends.next()) {
                int friendId = commonFriends.getInt("friend_id");
                commonFriendsList.add(getUser(friendId).get());
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();

        }
        return commonFriendsList;

    }
/*
    @Override
    public List<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        List<User> commonFriendsList = new ArrayList<>();
        List<User> firstUserFriends = getFriends(firstUserId);
        List<User> secondUserFriends = getFriends(secondUserId);

        for (User u : firstUserFriends) {
            if (secondUserFriends.contains(u)) {
                commonFriendsList.add(u);
            }
        }
        return commonFriendsList;
    }*/


    @Override
    public boolean deleteUser(int userId) {
        try {
            String sqlQuery = "delete from Users_DB where User_id = ?";
            return jdbcTemplate.update(sqlQuery, userId) > 0;
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        try {
            String sqlQuery = "delete from Friends where User_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        SqlRowSet firstId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", userId);
        SqlRowSet secondId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", friendId);

        if (!firstId.next() || !secondId.next()) {
            throw new DataBaseNotFoundException();
        }

        try {
            String sqlQuery = "insert into Friends(user_id, friend_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    userId,
                    friendId);
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }

    @Override
    public User addUser(User user) {
        userValidate(user);
        int nextId = 1;

        SqlRowSet existingId = jdbcTemplate.queryForRowSet("SELECT MAX(user_id) FROM USERS_DB");
        if (existingId.next()) {
            nextId = existingId.getInt("MAX(user_id)") + 1;
        }
        try {
            String sqlQuery = "insert into Users_DB(User_id, name, email, login, birthday) " +
                    "values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    nextId,
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday());

            SqlRowSet userId = jdbcTemplate
                    .queryForRowSet("select User_id from Users_DB where name = ? AND email = ? AND login = ?",
                            user.getName(),
                            user.getEmail(),
                            user.getLogin());
            if (userId.next()) {
                user.setId(userId.getInt("User_id"));
            }

            for (int friendId : user.getFriends()) {
                String sqlQueryFriends = "insert into Friends(User_id, Friend_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryFriends,
                        user.getId(),
                        friendId);
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet userId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", user.getId());
        if (!userId.next()) {
            throw new DataBaseNotFoundException();
        }

        try {
            String sqlQuery = "update Users_DB set " + "Name = ?, Email = ?, Login = ?, Birthday = ? "
                    + "where User_id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getId());

        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return user;
    }

    private Optional<User> getSingleUserRecord(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    LocalDate.parse(userRows.getString("birthday")));
            user.setId(id);
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?", id);
            while (friendRows.next()) {
                String value = friendRows.getString("friend_id");
                user.getFriends().add(Integer.valueOf(value));
            }

            log.info("Найден пользователь: {} {}",
                    userRows.getString("user_id"), userRows.getString("name"));

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private void userValidate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (!emailValidate(user.getEmail())) {
            throw new DataBaseException("Электронная почта введена в неверном формате.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new DataBaseException("Дата рождения введена в неверном формате.");
        }
    }

    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}