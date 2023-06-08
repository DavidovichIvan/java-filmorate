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
@Component //не забыть для FilmDao тоже добавить аннотацию
public class UserDao implements UserStorageDB { //пока что не добавлял интерфейс; когда будет работать все = удалить инмемори и старые сервисы-контроллеры а тут интерфейс добавить
    //   private final Logger log = LoggerFactory.getLogger(UserDao.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> getUser(Integer id) {
        return getSingleUserRecord(id);
    }


    static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

       try {
           SqlRowSet usersId = jdbcTemplate.queryForRowSet("select * from Users_DB"); //получили всех существующих пользователей (точнее все записи из таблицы)
           while (usersId.next()) {  //прошлись по каждой записи и дернули id пользователя
               Integer userId = usersId.getInt("user_id");
               Optional<User> optUser = getSingleUserRecord(userId); //своим методом (внизу) извлекли по каждому id и создали объект-юзер
               allUsers.add(optUser.get()); //записали в цикле каждого юзера в список
           }
       }  catch(RuntimeException r) {
        throw new DataBaseException();
    }
        return allUsers;
    }

    public List<User> getFriends(Integer id) {
        List<User> allFriends = new ArrayList<>();

        try{
        SqlRowSet friendsId = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?", id);
        while (friendsId.next()) {
            Integer friendId = Integer.valueOf(friendsId.getString("friend_id"));
            Optional<User> optFriend = getSingleUserRecord(friendId);
            allFriends.add(optFriend.get());
        }
        } catch(RuntimeException r) {
            throw new DataBaseException();
        }
        return allFriends;
    }

    public Map<Integer, User> getUsersList() {
        List<User> allUsers = getAllUsers();
        Map<Integer, User> usersMap = new HashMap<>();
        for (User u : getAllUsers()) {
            usersMap.put(u.getId(), u);
        }
        return usersMap;
    }

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
    }

    public boolean deleteUser(int userId) {
        try {
            String sqlQuery = "delete from Users_DB where User_id = ?";
            return jdbcTemplate.update(sqlQuery, userId) > 0;
        } catch(RuntimeException r) {
        throw new DataBaseException();
    }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        try{
        String sqlQuery = "delete from Friends where User_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch(RuntimeException r) {   //тут надо разбираться что ловить
            throw new DataBaseException();
        }
    }

    public void addFriend(Integer userId, Integer friendId) {
        //надо проверить что такие есть в базе id

        SqlRowSet firstId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", userId);
        SqlRowSet secondId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", friendId);

        if (!firstId.next() || !secondId.next()){  //проверили есть ли индекс юзера которого надо апдейтить в базе
            throw new DataBaseNotFoundException();
        }

        try{
        String sqlQuery = "insert into Friends(user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
        } catch(RuntimeException r) {
            throw new DataBaseException();
        }
        }

    public User addUser(User user) {  //исхожу из того что id присваивает база сама
     userValidate(user);

        int nextId = 1;

        SqlRowSet existingId = jdbcTemplate.queryForRowSet("SELECT MAX(user_id) FROM USERS_DB");

        if (existingId.next()) {
           nextId = existingId.getInt("MAX(user_id)") + 1; //это пздц; ВНИМАТЕЛЬНО, так как в запросе выше по результатам исполнения поле называется уже не user_id а MAX(user_id) - то и здесь в объекте обращаться к нему нужно точно также
        }

            try {
            String sqlQuery = "insert into Users_DB(User_id, name, email, login, birthday) " + //если тесты будут голову делать с id пользователя можно  убрать отсюда чтобы база сама присваивала
                    "values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                   nextId,
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday()); //записали объект в базу; id юзера присвоилось базой;
            // в принципе тут можно остановиться но так как метод возвращает юзера в теле ответа нужно этому возвращаемому юзеру присвоить id из базы

            SqlRowSet userId = jdbcTemplate.queryForRowSet("select User_id from Users_DB where name = ? AND email = ? AND login = ?", //тут считали id зная другие данные пользователя
                    user.getName(),
                    user.getEmail(),
                    user.getLogin());

            if (userId.next()) {
                user.setId(userId.getInt("User_id"));
            }

//второй инсерт должен быть на добавление списка друзей
        for (int friendId : user.getFriends()) {
            String sqlQueryFriends = "insert into Friends(User_id, Friend_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryFriends,
                    user.getId(),
                    friendId);
        }

        } catch(RuntimeException r) {
                     throw new DataBaseException();
        }

        return user;
    }

    public User updateUser(User user) {

        SqlRowSet userId = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", user.getId());

       if (!userId.next()){  //проверили есть ли индекс юзера которого надо апдейтить в базе
               throw new DataBaseNotFoundException();
                   }

        try{
        String sqlQuery = "update Users_DB set " + "Name = ?, Email = ?, Login = ?, Birthday = ? "
                + "where User_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());

        //тут по идее еще бы добавить запрос на обновление списка друзей (пока опустим до АТ)
       } catch(RuntimeException r) {   //тут надо разбираться что ловить
           throw new DataBaseException();
       }
        return user;
    }

    private Optional<User> getSingleUserRecord(Integer id) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from Users_DB where user_id = ?", id);

        if (userRows.next()) {
            User user = new User(
                    //  id,
                    userRows.getString("email"), //здесь внимательно, порядок строк не такой как в БД а такой как в конструкторе прописан (бала ошибка у меня)
                    userRows.getString("login"),
                    userRows.getString("name"),
                    LocalDate.parse(userRows.getString("birthday")));
            user.setId(id);
            // по аналогии заполнение friends через запрос к другой таблице
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?", id);
            while (friendRows.next()) {
                String value = friendRows.getString("friend_id");
                user.getFriends().add(Integer.valueOf(value));
            }

            log.info("Найден пользователь: {} {}", userRows.getString("user_id"), userRows.getString("name"));

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private void userValidate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin()); //если имя пустое то логин присвоили ему
        }

      if(!emailValidate(user.getEmail())) {
            throw new DataBaseException("Электронная почта введена в неверном формате.");
        }

      if(user.getBirthday().isAfter(LocalDate.now())) {
          throw new DataBaseException("Дата рождения введена в неверном формате.");
      }



    }

    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }



}
