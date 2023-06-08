package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Film {
   // @Getter
   // @Setter
   // private static int filmIdCounter = 1; //наверное убрать в итоге, за это будет овечать БД

    @Getter
    @Setter
    private int id;

    @NotBlank(message = "Название не может быть пустым") //БГУб
    private String name;
    @Size(max = 200, message = "Максимальная длина описание 200 символов") //БГУб
    private String description;

    @Getter
    @Setter
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;

    private final MPA mpa;

    // private Set<Integer> genres;//по мне так достаточно виде просто списка значений, но в автотестах там сделан список объектов класса
    @Getter
    @Setter
    private Set<Genre> genres;
    @Getter
    @Setter
    private Set<Integer> likes;

    @Getter
    @Setter
    int rate;

    public Film(String name, String description, LocalDate releaseDate, int durationInMinutes, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = durationInMinutes;

        this.mpa = mpa; //пока что добавил просто поле; но по идее в какой-то момент должна быть валидация, что рейтинг переданный содержится в БД
        //для валидации можно метод прописать в классе VariblesValidation например; ну или прямо в конструкторе;
        //для этого при загрузке программы сначала надо выгружать из БД наименования всех рейтингов в какой нить список или мапу и проверять что переданный рейтинг есть в значениях этой мапы

        //this.genres = new HashSet<>();
        this.genres = new TreeSet<>((o1, o2) -> o1.getId() - o2.getId());
        this.likes = new HashSet<>();




        //здесь прописать обработчик строки genre = чтобы переводил в Set<String> genre

    //    this.id = filmIdCounter; //убрать когда все будет работать
     //   filmIdCounter++; //убрать потом и в конце попробовать вместо конструктора аннотацию @Data
    }

}
