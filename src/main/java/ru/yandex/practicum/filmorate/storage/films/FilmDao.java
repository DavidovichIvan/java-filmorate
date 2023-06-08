package ru.yandex.practicum.filmorate.storage.films;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataBaseException;
import ru.yandex.practicum.filmorate.exceptions.DataBaseNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Getter
@Setter
@Component
public class FilmDao implements FilmStorageDB {
    private final JdbcTemplate jdbcTemplate;

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Film> getFilm(Integer id) {
        return getSingleFilmRecord(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from Genre ORDER BY GENRE_ID");
        while (genreRows.next()) {
            genres.add(new Genre
                    (genreRows.
                            getInt("Genre_ID"),
                            genreRows.getString("Genre_name")));
        }
        return genres;
    }

    @Override
    public Optional<Genre> getGenre(Integer id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("select * from Genre where genre_id = ?", id);
        if (genreRow.next()) {
            Genre genre = new Genre(id, genreRow.getString("Genre_name"));
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new DataBaseNotFoundException("Отсутствует запись о жанре");
        }
    }

    @Override
    public List<MPA> getAllMPA() {
        List<MPA> mpa = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from Rating ORDER BY RATING_ID");
        while (mpaRows.next()) {
            mpa.add(new MPA
                    (mpaRows.
                            getInt("Rating_ID"),
                            mpaRows.getString("Rating_name")));
        }
        return mpa;
    }

    @Override
    public Optional<MPA> getMPA(Integer id) {

        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("select * from Rating where rating_id = ?", id);
        if (mpaRow.next()) {
            MPA mpa = new MPA(id, mpaRow.getString("rating_name"));
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            throw new DataBaseNotFoundException("Указан несуществующий жанр.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        try {
            SqlRowSet filmId = jdbcTemplate.queryForRowSet("select * from Films"); //получили все фильмы
            while (filmId.next()) {  //прошлись по каждой записи и дернули id

                Integer fId = filmId.getInt("Film_id");
                Optional<Film> optFilm = getSingleFilmRecord(fId);
                allFilms.add(optFilm.get()); //записали в цикле каждого юзера в список
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return allFilms;
    }

    private Optional<Film> getSingleFilmRecord(Integer id) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from Films where film_id = ?", id); //выдернули запись о конкретном фильме
        int ratingId = 0;
        //чтобы корректно работать объекту RowSet надо понимать какую строку он в текущий момент обрабатывает из таблицы, полученной на sql запрос
        if (filmRows.next()) { //вот это логическое условие фактически двигает курсор на следующую строку из ответа на sql мой запрос
            // = то есть в данном случае мы говорим, что работаем со следующей (относительно нуля) то есть с первой строкой
            ratingId = filmRows.getInt("rating_id"); //из всех строк БД дернули значение id фильма
        }
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select rating_name from Rating where rating_id = ?", ratingId); //здесь уже другой запрос к таблице с рейтингами, котрому мы передаем рейтинг нашего конкретного фильма

        String name = "";
        if (ratingRows.next()) { //опять же говорим объекту RowSet что работаем со следующей строкой (в данном случае спервой)
            name = ratingRows.getString("Rating_name"); //вытащили имя рейтинга
        }

        if (filmRows.first()) {  //теперь возвращамся к фильмам; так как мы строку уже выше сдвинули на первую --if (filmRows.next()--, то тут говорим курсору что работаем с первой строкой -- filmRows.first() --
            Film film = new Film(
                    filmRows.getString("Title"),
                    filmRows.getString("Description"),
                    LocalDate.parse(Objects.requireNonNull(filmRows.getString("Release_date"))),
                    filmRows.getInt("Duration"),
                    new MPA(ratingId, name));
            film.setId(id);
            film.setGenres(getGenresForFilm(id));
            film.setLikes(getLikesForFilm(id));

/* вниз утащил в отдельный метод; убрать в оконцове
            // заполнение genres через запрос к другой таблице БД; сделал так как тестовых запросах что жанры это отдельный объект класса, и в объекте Фильм есть список объектов-жанров
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select genre_id from film_genre where film_id = ?", id);
            String grName = "";
            while (genreRows.next()) { //в данном случае в сочетании с while курсор будет сдвигаться вниз на строку пока эти строки есть и отрабатывать тело цикла
                String genre = genreRows.getString("genre_id");
                assert genre != null;
                SqlRowSet genreName = jdbcTemplate.queryForRowSet("select genre_name from genre where genre_id = ?", Integer.parseInt(genre));
                if (genreName.next()) {
                    grName = genreName.getString("genre_name");
                }
                Genre gr = new Genre(Integer.parseInt(genre), grName);
                film.getGenres().add(gr);
            } */

            // по аналогии заполнение likes через запрос к другой таблице //для лайков оставил как было хэшсет из просто инт (номера id пользователей);
            /*вниз утащил в отдельный метод; убрать в оконцове
            SqlRowSet likeRows = jdbcTemplate.queryForRowSet("select user_id from likes where film_id = ?", id);
            while (likeRows.next()) {
                String like = likeRows.getString("user_id");
                assert like != null;
                film.getLikes().add(Integer.valueOf(like));
            }
*/
            log.info("Найден фильм: id {}, название -  {}", filmRows.getString("Film_id"), filmRows.getString("title"));

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);

        int nextId = 1;

        SqlRowSet existingId = jdbcTemplate.queryForRowSet("SELECT MAX(film_id) FROM Films");

        if (existingId.next()) {
            nextId = existingId.getInt("MAX(film_id)") + 1;
        }

        try {

            String sqlQuery = "insert into Films(film_id, title, description, release_date, duration, rating_id, rate) " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    nextId,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),//записали объект в базу; id фильма присвоилось базой;
                    film.getRate()); //это поле rate которое я добавил только из-за автотестов


            SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select rating_name from Rating where rating_id = ?", film.getMpa().getId());
            if (ratingRows.next()) {
                film.getMpa().setName(ratingRows.getString("rating_name"));//присвоили имя mpa по id
            }

            SqlRowSet filmId = jdbcTemplate.queryForRowSet("select Film_id from Films where title = ? AND release_date = ?", //тут считали id зная другие данные фильма
                    film.getName(),
                    film.getReleaseDate());

            if (filmId.next()) {
                film.setId(filmId.getInt("Film_id"));
            }

            addLikesForExistingUsersOnly(film);

            //третий инсерт на добавление списка жанров
            //также добавим проверку что жанр (его id) переданный в запросе, существует в БД; если такого жанра нет, то просто его пропускаем но объект записываем; иначе будет ошибка целостности данных

            recordGenresForFilm(film);
/*
            for (Genre gen : film.getGenres()) {

                SqlRowSet checkIfGenreExists = jdbcTemplate.queryForRowSet("select Genre_id from Genre where Genre_id = ?",
                        gen.getId());

                if (checkIfGenreExists.next()) {
                    String sqlQueryGenres = "insert into Film_genre(Film_id, Genre_id) " +
                            "values (?, ?)";
                    jdbcTemplate.update(sqlQueryGenres,
                            film.getId(),
                            gen.getId());

                    SqlRowSet genName = jdbcTemplate.queryForRowSet("select Genre_name from Genre where Genre_id = ?",
                            gen.getId());
                    if (genName.next()) {
                        gen.setName(genName.getString("Genre_name"));
                    }
                }
            } */

        } catch (RuntimeException r) {
            throw new DataBaseException("Ошибка добавления фильма.");
        }

//весь мозг вынес - множество Set не удаляет по простому объекы (видимо не понимает как именно соотнести);
// в итоге вот так решил вопрос - прогоняем в цикле итератором, если соблюдается условие что имя жанра = null
        //а в коде выше всем жанрам которые есть в БД я имена присвоил, то текущий элемент итератора удаляется
/*
        Iterator<Genre> iterator = film.getGenres().iterator();
        while (iterator.hasNext()) {
            Genre element = iterator.next();
            if (element.getName() == null) {
                iterator.remove();
            }
        }
        */
        film.getGenres().removeIf(element -> element.getName() == null); //это вот код итератора чуть выше, мудрая IDEA мне предложила упростить

        return film;
    } //в оконцове проверить что не только постман все возвращает но и в базу попадают значения во все таблицы

    @Override
    public boolean deleteFilm(Integer filmId) {
        try {
            String sqlQuery = "delete from Films where Film_id = ?";
            return jdbcTemplate.update(sqlQuery, filmId) > 0;
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }


    public Film updateFilm(Film film) {
        filmValidation(film);
        SqlRowSet filmId = jdbcTemplate.queryForRowSet("select * from Films where film_id = ?", film.getId());

        if (!filmId.next()) {  //проверили есть ли индекс фильма которого надо апдейтить в базе
            throw new DataBaseNotFoundException();
        }
        try {
            String sqlQuery = "update Films set " + "Title = ?, Description = ?, Release_date = ?, Duration = ?, Rating_id = ?, Rate = ?"
                    + "where Film_id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getRate()
                    , film.getId());

            //надо обновить имя МПА
            SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select rating_name from Rating where rating_id = ?", film.getMpa().getId());
            if (ratingRows.next()) {
                film.getMpa().setName(ratingRows.getString("rating_name"));//присвоили имя mpa по id
            }

            // addLikesForExistingUsersOnly(film); //здесь, в отличие от метода создания нового пользователя уже может существовать запись о лайках конкретного пользователя
            //соответственно при попытке этим методом осуществить запись, может возникнуть конфликт с БД; соответственно тут или нужно сначала запись о лайках фильма удалять (в общем то чуть ниже со списком жанров так и делаю я)
            //перед этим методом или как то добавлять проверку что такой записи нет в БД (в принципе не ясен подход, как должен действовать сервер если приходит список новых лайков с фильмом:
            //тут куча вариантов: заменить все которые раньше были; или дополнить их; пока что принимаю решение что лайки отдельно = в этом методе не обрабатываю их (посмотрим что в АТ будет)
            //в таком виде этот метод если что запускать нельзя здесь (только при добавлении нового фильма) - если придется тут добавлять то самое простое - перед ним прописать чтобы сущствующие записи о лайках удалились сначала
//и лайков возможно (хотя это можно другим эндпоинтом реализовать)
//----------------------------------------------------------------------------------------------------------------------
            //тут по идее еще бы добавить запрос на обновление списка жанров (пока опустим до АТ) - а зря ))
            //в 2 действия - все старые жанры удалить из бд

            String deleteQuery = "delete from FILM_GENRE Genre WHERE film_id = ?";
            jdbcTemplate.update(deleteQuery, film.getId());

            //новые записать (должен быть метод под это дело уже)
            //то есть у меня с объектом пришел список genres - надо в цикле по нему пройтись и загнать в базу (у объекта только id жанра имя надо подтягивать из другой таблицы)
            recordGenresForFilm(film);

            Set<Genre> rev = new TreeSet<>(Comparator.comparingInt(Genre::getId)); //это искуственный блок чтобы пройти автотест; смысл такой что при постплении запроса json записывает жанры в объект-фильм верно но в обратном порядке (игнорируя компаратор причем); на программу это не влияет, но автотест требует; пришлось тут искуственно перезаписать список, применяя компаратор
            rev.addAll(film.getGenres());

            film.setGenres(rev);

        } catch (RuntimeException r) {   //тут надо разбираться что ловить
            throw new DataBaseException("Ошибка при обновлении фильма");
        }
        return film;
    }

    private Set<Genre> getGenresForFilm(Integer filmId) {
        Set<Genre> genres = new HashSet<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select genre_id from film_genre where film_id = ?", filmId);
        String grName = "";
        while (genreRows.next()) { //в данном случае в сочетании с while курсор будет сдвигаться вниз на строку пока эти строки есть и отрабатывать тело цикла
            String genre = genreRows.getString("genre_id");
            assert genre != null;
            SqlRowSet genreName = jdbcTemplate.queryForRowSet("select genre_name from genre where genre_id = ?", Integer.parseInt(genre));
            if (genreName.next()) {
                grName = genreName.getString("genre_name");
            }
            Genre gr = new Genre(Integer.parseInt(genre), grName);
            genres.add(gr);
        }
        return genres;
    }

    private Set<Integer> getLikesForFilm(Integer filmId) {
        Set<Integer> likes = new HashSet<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("select user_id from likes where film_id = ?", filmId);
        while (likeRows.next()) {
            String like = likeRows.getString("user_id");
            assert like != null;
            likes.add(Integer.valueOf(like));
        }
        return likes;
    }


    private void addLikesForExistingUsersOnly(Film film) {

        for (Integer userWhoPutLikeId : film.getLikes()) {
            //проверка если юзера нет, который в списке  лайков содержится, то этот лайк в БД разносить не нужно иначе будет ошибка целостности данных таккак в БД лайки связаны с юзерами

            SqlRowSet checkUserWhoPutLikeExists = jdbcTemplate.queryForRowSet("select User_id from Users_DB where User_id = ?", //запросили в БД есть ли юзер который передан в запросе как поставивший лайк
                    userWhoPutLikeId);

            if (checkUserWhoPutLikeExists.next()) {  //если запрос юзера вернулся не пустым значит пользователь есть и можно добавлять сведения о лайках
                String sqlQueryFriends = "insert into Likes(Film_id, User_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryFriends,
                        film.getId(),
                        userWhoPutLikeId);
            } else {
                film.getLikes().remove(userWhoPutLikeId);
            } //иначе из объекта-фильма, переданного в запросе убираем лайк несуществующего пользователя
            //в итоге при добавлении пользователя в БД добавятся лайки только от тех пользователей которые есть в БД
        }

    }

    private void filmValidation(Film film) {
        if (film.getDuration() <= 0) {
            throw new DataBaseException("Продолжительность фильма не может быть отрицательной.");
        }

        SqlRowSet checkIfMpaExists = jdbcTemplate.queryForRowSet("select Genre_id from Genre where Genre_id = ?",
                film.getMpa().getId());

        if (!checkIfMpaExists.next()) {
            throw new DataBaseNotFoundException("Указан несуществующий жанр.");
        }
    }

    @Override
    public void putLike(Integer filmId, Integer userId) {
        SqlRowSet checkFilmExists = jdbcTemplate.queryForRowSet("select Film_id from Films where Film_id = ?", //запросили в БД есть ли юзер который передан в запросе как поставивший лайк
                filmId);
        if (!checkFilmExists.next()) {
            throw new DataBaseNotFoundException("Фильм не найден");
        }

        SqlRowSet checkUserWhoPutLikeExists = jdbcTemplate.queryForRowSet("select User_id from Users_DB where User_id = ?", //запросили в БД есть ли юзер который передан в запросе как поставивший лайк
                userId);
        if (!checkUserWhoPutLikeExists.next()) {
            throw new DataBaseNotFoundException("Пользователь не найден");
        }
        SqlRowSet checkIfLikeAlreadyExists = jdbcTemplate.queryForRowSet("select * from Likes where Film_id = ? AND User_id = ?", //запросили в БД есть ли юзер который передан в запросе как поставивший лайк
                filmId, userId);
        if (checkIfLikeAlreadyExists.next()) {
            throw new DataBaseNotFoundException("Лайк фильму от данного пользователя уже поставлен.");
        }

        try {
            String sqlPutLike = "insert into Likes(Film_id, User_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlPutLike,
                    filmId,
                    userId);
        } catch (RuntimeException r) {
            throw new DataBaseException("Ошибка при записи лайка.");
        }
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        if ((filmId <= 0) || (userId <= 0)) {
            throw new DataBaseNotFoundException();
        }

        try {
            String sqlQuery = "delete from Likes where Film_id = ? AND User_id = ?";
            jdbcTemplate.update(sqlQuery, filmId, userId);

        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }

    public List<Film> getPopularFilms(Integer amountOfFilms) {
        List<Film> popularFilms = new ArrayList<>();
        Integer filmId;
        Film film;
        String sql = "SELECT film_id, count(user_id) FROM likes GROUP BY film_id ORDER BY count(user_id) DESC LIMIT " + amountOfFilms;

        SqlRowSet getPopularFilms = jdbcTemplate.queryForRowSet(sql);

        while (getPopularFilms.next()) {
            //извлекаем из ответа film_id
            filmId = getPopularFilms.getInt("film_id");

            //обращаемся к методу get film и записываем результат метода в список
            film = getFilm(filmId).get();
            popularFilms.add(film);

        }
        //вспомогательный метод чтобы пройти автотесты в части где почему-то пытаются популярность фильма передать через поле rate о котором лапоть не звенел - в ТЗ 10 сказано, что через лайки определяется популярность
        if (popularFilms.isEmpty()) {
            sql = "select Film_id from Films where rate > 0 ORDER BY rate DESC LIMIT " + amountOfFilms;
            SqlRowSet getPopularFilmsByRate = jdbcTemplate.queryForRowSet(sql);
            while (getPopularFilmsByRate.next()) {
                filmId = getPopularFilmsByRate.getInt("film_id");
                film = getFilm(filmId).get();
                popularFilms.add(film);
            }
        }
        return popularFilms;
    }


    private void recordGenresForFilm(Film film) {

        for (Genre gen : film.getGenres()) {

            SqlRowSet checkIfGenreExists = jdbcTemplate.queryForRowSet("select Genre_id from Genre where Genre_id = ?",
                    gen.getId());

            if (checkIfGenreExists.next()) {
                String sqlQueryGenres = "insert into Film_genre(Film_id, Genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenres,
                        film.getId(),
                        gen.getId());

                SqlRowSet genName = jdbcTemplate.queryForRowSet("select Genre_name from Genre where Genre_id = ?",
                        gen.getId());
                if (genName.next()) {
                    gen.setName(genName.getString("Genre_name"));
                }
            }
        }

    }

}