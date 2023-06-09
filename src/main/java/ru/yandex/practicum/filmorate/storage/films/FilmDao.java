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
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        return getSingleFilmRecord(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from Genre ORDER BY GENRE_ID");
        while (genreRows.next()) {
            genres.add(new Genre(
                    genreRows
                            .getInt("Genre_ID"),
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
            mpa.add(new MPA(mpaRows
                    .getInt("Rating_ID"),
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
            SqlRowSet filmId = jdbcTemplate.queryForRowSet("select * from Films");
            while (filmId.next()) {

                Integer fId = filmId.getInt("Film_id");
                Optional<Film> optFilm = getSingleFilmRecord(fId);
                allFilms.add(optFilm.get());
            }
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
        return allFilms;
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
                    film.getMpa().getId(),
                    film.getRate());

            SqlRowSet ratingRows = jdbcTemplate
                    .queryForRowSet("select rating_name from Rating where rating_id = ?", film.getMpa().getId());
            if (ratingRows.next()) {
                film.getMpa().setName(ratingRows.getString("rating_name"));
            }
            SqlRowSet filmId = jdbcTemplate
                    .queryForRowSet("select Film_id from Films where title = ? AND release_date = ?",
                            film.getName(),
                            film.getReleaseDate());

            if (filmId.next()) {
                film.setId(filmId.getInt("Film_id"));
            }

            addLikesForExistingUsersOnly(film);
            recordGenresForFilm(film);
        } catch (RuntimeException r) {
            throw new DataBaseException("Ошибка добавления фильма.");
        }
        film.getGenres().removeIf(element -> element.getName() == null);

        return film;
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        try {
            String sqlQuery = "delete from Films where Film_id = ?";
            return jdbcTemplate.update(sqlQuery, filmId) > 0;
        } catch (RuntimeException r) {
            throw new DataBaseException();
        }
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidation(film);
        SqlRowSet filmId = jdbcTemplate.queryForRowSet("select * from Films where film_id = ?", film.getId());

        if (!filmId.next()) {
            throw new DataBaseNotFoundException();
        }
        try {
            String sqlQuery = "update Films set "
                    + "Title = ?, Description = ?, Release_date = ?, Duration = ?, Rating_id = ?, Rate = ?"
                    + "where Film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getRate(),
                    film.getId());

            SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select rating_name from Rating where rating_id = ?",
                    film.getMpa().getId());
            if (ratingRows.next()) {
                film.getMpa().setName(ratingRows.getString("rating_name"));
            }

            String deleteQuery = "delete from FILM_GENRE Genre WHERE film_id = ?";
            jdbcTemplate.update(deleteQuery, film.getId());

            recordGenresForFilm(film);
            Set<Genre> rev = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            rev.addAll(film.getGenres());

            film.setGenres(rev);

        } catch (RuntimeException r) {
            throw new DataBaseException("Ошибка при обновлении фильма");
        }
        return film;
    }

    @Override
    public void putLike(Integer filmId, Integer userId) {
        SqlRowSet checkFilmExists = jdbcTemplate
                .queryForRowSet("select Film_id from Films where Film_id = ?",
                        filmId);
        if (!checkFilmExists.next()) {
            throw new DataBaseNotFoundException("Фильм не найден");
        }

        SqlRowSet checkUserWhoPutLikeExists = jdbcTemplate
                .queryForRowSet("select User_id from Users_DB where User_id = ?",
                        userId);
        if (!checkUserWhoPutLikeExists.next()) {
            throw new DataBaseNotFoundException("Пользователь не найден");
        }
        SqlRowSet checkIfLikeAlreadyExists = jdbcTemplate
                .queryForRowSet("select * from Likes where Film_id = ? AND User_id = ?",
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
        int filmId;
        Film film;
        String sql = "SELECT film_id, count(user_id) FROM likes GROUP BY film_id ORDER BY count(user_id) DESC LIMIT " +
                amountOfFilms;

        SqlRowSet getPopularFilms = jdbcTemplate.queryForRowSet(sql);

        while (getPopularFilms.next()) {
            filmId = getPopularFilms.getInt("film_id");
            film = getFilm(filmId).get();
            popularFilms.add(film);
        }

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

    private Set<Genre> getGenresForFilm(Integer filmId) {
        Set<Genre> genres = new HashSet<>();

        SqlRowSet genreRows = jdbcTemplate
                .queryForRowSet("select genre_id from film_genre where film_id = ?", filmId);
        String grName = "";
        while (genreRows.next()) {
            String genre = genreRows.getString("genre_id");
            assert genre != null;
            SqlRowSet genreName = jdbcTemplate
                    .queryForRowSet("select genre_name from genre where genre_id = ?", Integer.parseInt(genre));
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
        SqlRowSet likeRows = jdbcTemplate
                .queryForRowSet("select user_id from likes where film_id = ?", filmId);
        while (likeRows.next()) {
            String like = likeRows.getString("user_id");
            assert like != null;
            likes.add(Integer.valueOf(like));
        }
        return likes;
    }

    private void addLikesForExistingUsersOnly(Film film) {
        for (Integer userWhoPutLikeId : film.getLikes()) {
            SqlRowSet checkUserWhoPutLikeExists = jdbcTemplate
                    .queryForRowSet("select User_id from Users_DB where User_id = ?", userWhoPutLikeId);

            if (checkUserWhoPutLikeExists.next()) {
                String sqlQueryFriends = "insert into Likes(Film_id, User_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryFriends,
                        film.getId(),
                        userWhoPutLikeId);
            } else {
                film.getLikes().remove(userWhoPutLikeId);
            }
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

    private void recordGenresForFilm(Film film) {
        for (Genre gen : film.getGenres()) {
            SqlRowSet checkIfGenreExists = jdbcTemplate
                    .queryForRowSet("select Genre_id from Genre where Genre_id = ?",
                            gen.getId());
            if (checkIfGenreExists.next()) {
                String sqlQueryGenres = "insert into Film_genre(Film_id, Genre_id) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenres,
                        film.getId(),
                        gen.getId());

                SqlRowSet genName = jdbcTemplate
                        .queryForRowSet("select Genre_name from Genre where Genre_id = ?",
                                gen.getId());
                if (genName.next()) {
                    gen.setName(genName.getString("Genre_name"));
                }
            }
        }
    }

    private Optional<Film> getSingleFilmRecord(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from Films where film_id = ?", id);
        int ratingId = 0;
        if (filmRows.next()) {
            ratingId = filmRows.getInt("rating_id");
        }
        SqlRowSet ratingRows = jdbcTemplate
                .queryForRowSet("select rating_name from Rating where rating_id = ?", ratingId);
        String name = "";
        if (ratingRows.next()) {
            name = ratingRows.getString("Rating_name");
        }

        if (filmRows.first()) {
            Film film = new Film(
                    filmRows.getString("Title"),
                    filmRows.getString("Description"),
                    LocalDate.parse(Objects.requireNonNull(filmRows.getString("Release_date"))),
                    filmRows.getInt("Duration"),
                    new MPA(ratingId, name));
            film.setId(id);
            film.setGenres(getGenresForFilm(id));
            film.setLikes(getLikesForFilm(id));
            log.info("Найден фильм: id {}, название -  {}",
                    filmRows.getString("Film_id"),
                    filmRows.getString("title"));

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}