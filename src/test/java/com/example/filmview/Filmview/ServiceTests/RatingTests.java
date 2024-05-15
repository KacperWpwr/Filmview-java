package com.example.filmview.Filmview.ServiceTests;


import com.example.filmview.Film.Film;
import com.example.filmview.Film.IFilmService;
import com.example.filmview.Rating.DTO.RatingListDTO;
import com.example.filmview.Rating.Rating;
import com.example.filmview.Rating.RatingRepository;
import com.example.filmview.Rating.RatingService;
import com.example.filmview.Rating.Requests.AddRatingRequest;
import com.example.filmview.Security.JWT.IJWTService;
import com.example.filmview.User.IUserService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RatingTests {
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private IFilmService filmService;
    @Mock
    private IUserService userService;
    @Mock
    private IJWTService ijwtService;

    @InjectMocks
    private RatingService ratingService;


    private Film testFilm;
    private User testUser;

    @BeforeEach
    public void CreateTestObjects(){
        System.out.println("Creating");
        testFilm = Film.builder()
                .ratings(new ArrayList<>())
                .directors(new ArrayList<>())
                .actors(new ArrayList<>())
                .title("Test Film")
                .description("Test Description")
                .id(1l)
                .build();

        testUser = User.builder()
                .is_blocked(false)
                .is_enabled(true)
                .role(UserRole.USER)
                .password("54321")
                .email("testuser@gmail.com")
                .login("testuser@gmail.com")
                .build();

    }

    @Test
    public void AddRating_Success(){
        AddRatingRequest request = new AddRatingRequest(1l,5,"description");
        Rating rating = Rating.builder()
                .rating(5)
                .description("description")
                .rated_film(testFilm)
                .rating_user(testUser)
                .Id(1)
                .build();

        when(ijwtService.extractUsername("token")).thenReturn("testuser@gmail.com");
        when(filmService.getFilm(1)).thenReturn(testFilm);
        when(filmService.saveFilm(Mockito.any(Film.class))).thenReturn(testFilm);
        when(userService.getUserByUsername("testuser@gmail.com")).thenReturn(testUser);
        when(ratingRepository.save(Mockito.any(Rating.class))).thenReturn(rating);

        RatingListDTO dto = ratingService.addRating(request,"Bearer token");

        assertThat(dto).isNotNull();
        assertThat(dto.ratings()).isNotNull();
        assertThat(dto.ratings().size()).isEqualTo(1);

    }


}
