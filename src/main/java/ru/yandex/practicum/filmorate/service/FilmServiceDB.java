
    package ru.yandex.practicum.filmorate.service;

    import lombok.Getter;
    import lombok.Setter;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import ru.yandex.practicum.filmorate.model.Film;
    import ru.yandex.practicum.filmorate.model.Genre;
    import ru.yandex.practicum.filmorate.model.MPA;
    import ru.yandex.practicum.filmorate.storage.films.FilmStorageDB;

    import java.util.List;
    import java.util.Optional;

    @Slf4j
    @Getter
    @Setter
    @Service
    public class FilmServiceDB {
        private final FilmStorageDB filmStorage;
        private final UserServiceDB userService;

        @Autowired
        public FilmServiceDB(FilmStorageDB filmStorage, UserServiceDB userService) {
            this.filmStorage = filmStorage;
            this.userService = userService;
        }

        public Optional<Film> getFilm(String id) {
            int requestId = Integer.parseInt(id);
            return filmStorage.getFilm(requestId);
        }

        public List<Genre> getAllGenres() {
            return filmStorage.getAllGenres();
        }

        public Optional<Genre> getGenre(String id) {
            int genreId = Integer.parseInt(id);
            return filmStorage.getGenre(genreId);
        }

        public List<MPA> getAllMPA() {
            return filmStorage.getAllMPA();
        }

        public Optional<MPA> getMPA(String id) {
            int mpaId = Integer.parseInt(id);
            return filmStorage.getMPA(mpaId);
        }

        public List<Film> getAllFilms() {
            return filmStorage.getAllFilms();
        }

        public Film addFilm(Film film) {
            return filmStorage.addFilm(film);
        }

        public boolean deleteFilm(String id) {
            Integer filmId = Integer.valueOf(id);
            return filmStorage.deleteFilm(filmId);
        }

        public Film updateFilm(Film film) {
            return filmStorage.updateFilm(film);
        }

        public void putLike(String filmId, String userId) {
            int filmToPutLikeId = Integer.parseInt(filmId);
            int userWhoPutLikeId = Integer.parseInt(userId);

            filmStorage.putLike(filmToPutLikeId, userWhoPutLikeId);
        }

        public void deleteLike(String filmId, String userId) {
            int filmToDeleteLikeId = Integer.parseInt(filmId);
            int userWhoDeleteLikeId = Integer.parseInt(userId);

            filmStorage.deleteLike(filmToDeleteLikeId, userWhoDeleteLikeId);
        }

        public List<Film> getPopularFilms(String count) {
            int numberOfFilms = Integer.parseInt(count);
            return filmStorage.getPopularFilms(numberOfFilms);
        }




/*
        private void checkIfFilmExists(Integer filmId) {
            if (!filmStorage.getFilmsList().containsKey(filmId)) {
                throw new IdNotExistException();
            }
        }
    }

    */

}
