package com.example.filmview.Filmview.ServiceTests;

import com.example.filmview.Actor.IActorService;
import com.example.filmview.Director.IDirectorService;
import com.example.filmview.Film.DTO.FilmListDTO;
import com.example.filmview.Film.Film;
import com.example.filmview.Film.FilmRepository;
import com.example.filmview.Film.FilmService;
import com.example.filmview.FilmStar.IFilmStarService;
import com.example.filmview.Rating.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TopFilmsTests {
    @Mock
    private IFilmStarService filmStarService;
    @Mock
    private IActorService actorService;
    @Mock
    private IDirectorService directorService;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    private Film film1 = Film.builder().id(1l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();
    private Film film2 = Film.builder().id(2l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();
    private Film film3 = Film.builder().id(3l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();
    private Film film4 = Film.builder().id(4l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();
    private Film film5 = Film.builder()
            .id(5l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();
    private Film film6 = Film.builder()
            .id(6l).title("title").description("description")
            .actors(new ArrayList<>()).directors(new ArrayList<>()).ratings(List.of()).build();

    private static Stream<Arguments> provideArgsForRatingCheck() {
        return Stream.of(
                Arguments.of(List.of(1,2,3,4,5,6,7,8,9,10), 5.5f),
                Arguments.of(List.of(9,9,8,2), 7f),
                Arguments.of(List.of(5,6,6,6), 5.75f),
                Arguments.of(List.of(8,9,9,6,5,6,5), 6.85f)
        );
    }

    @Test
    public void FilmGetRating_NoRatings(){
        Float rating = film1.getRating();
        assertEquals(0f, rating);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10})
    public void FilmGetRating_OneRating(int val){
        film1.setRatings(List.of(new Rating(0l, null, val,null,null)));

        Float rating = film1.getRating();
        assertEquals((float)val,rating);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForRatingCheck")
    public void FilmGetRating_MultipleRating(List<Integer> ratingSource, float expectedResult){
        List<Rating> ratings = new ArrayList<>();

        for (Integer rating:ratingSource){
            ratings.add(new Rating(0l,null, rating, null,null));
        }
        film1.setRatings(ratings);

        Float rating = film1.getRating();
        assertEquals(expectedResult, rating);

    }

    @Test
    public void GetTopFilms_QuantityEqualToFilmNumber(){
        film1.setRatings(List.of(new Rating(1l,null,1,null,null)));
        film2.setRatings(List.of(new Rating(2l,null,2,null,null)));
        film3.setRatings(List.of(new Rating(3l,null,3,null,null)));
        film4.setRatings(List.of(new Rating(4l,null,4,null,null)));
        film5.setRatings(List.of(new Rating(5l,null,5,null,null)));

        List<Film> films = new ArrayList<>();
        films.addAll(List.of(film1,film2,film3,film4,film5));


        when(filmRepository.findAll()).thenReturn(films);

        FilmListDTO dto = filmService.getTopFilms(5);
        assertThat(dto).isNotNull();
        assertThat(dto.films()).isNotNull();
        assertThat(dto.films().size()).isEqualTo(5);
        assertEquals(5,dto.films().get(0).id());
        assertEquals(4,dto.films().get(1).id());
        assertEquals(3,dto.films().get(2).id());
        assertEquals(2,dto.films().get(3).id());
        assertEquals(1,dto.films().get(4).id());
    }

    @Test
    public void GetTopFilms_QuantityLessThanFilmNumber(){
        film1.setRatings(List.of(new Rating(1l,null,1,null,null)));
        film2.setRatings(List.of(new Rating(2l,null,2,null,null)));
        film3.setRatings(List.of(new Rating(3l,null,3,null,null)));
        film4.setRatings(List.of(new Rating(4l,null,4,null,null)));
        film5.setRatings(List.of(new Rating(5l,null,5,null,null)));
        film6.setRatings(List.of(new Rating(6l,null,6,null,null)));

        List<Film> films = new ArrayList<>();
        films.addAll(List.of(film1,film2,film3,film4,film5,film6));


        when(filmRepository.findAll()).thenReturn(films);

        FilmListDTO dto = filmService.getTopFilms(5);
        assertThat(dto).isNotNull();
        assertThat(dto.films()).isNotNull();
        assertThat(dto.films().size()).isEqualTo(5);
        assertEquals(6,dto.films().get(0).id());
        assertEquals(5,dto.films().get(1).id());
        assertEquals(4,dto.films().get(2).id());
        assertEquals(3,dto.films().get(3).id());
        assertEquals(2,dto.films().get(4).id());
    }

    @Test
    public void GetTopFilms_QuantityBiggerThanFilmNumber(){
        film1.setRatings(List.of(new Rating(1l,null,1,null,null)));
        film2.setRatings(List.of(new Rating(2l,null,2,null,null)));
        film3.setRatings(List.of(new Rating(3l,null,3,null,null)));
        film4.setRatings(List.of(new Rating(4l,null,4,null,null)));


        List<Film> films = new ArrayList<>();
        films.addAll(List.of(film1,film2,film3,film4));


        when(filmRepository.findAll()).thenReturn(films);

        FilmListDTO dto = filmService.getTopFilms(5);
        assertThat(dto).isNotNull();
        assertThat(dto.films()).isNotNull();
        assertThat(dto.films().size()).isEqualTo(4);
        assertEquals(4,dto.films().get(0).id());
        assertEquals(3,dto.films().get(1).id());
        assertEquals(2,dto.films().get(2).id());
        assertEquals(1,dto.films().get(3).id());
    }

}
