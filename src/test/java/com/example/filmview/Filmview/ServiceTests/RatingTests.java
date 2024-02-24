package com.example.filmview.Filmview.ServiceTests;


import com.example.filmview.Film.Film;
import com.example.filmview.Film.FilmService;
import com.example.filmview.Film.Requests.CreateFilmRequest;
import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarRepository;
import com.example.filmview.FilmviewApplication;
import com.example.filmview.Rating.RatingService;
import com.example.filmview.Rating.Requests.AddRatingRequest;
import com.example.filmview.Security.Authentication.AuthenticationService;
import com.example.filmview.Security.JWT.JWTService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRepository;
import com.example.filmview.User.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
public class RatingTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private FilmStarRepository filmStarRepository;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private JWTService jwtService;


    @BeforeEach
    public void fillDatabase(){
        userRepository.deleteAll();

        System.out.println("Adding users");
        addMockUser("user1@gmail.com","12345");
        addMockHeadAdmin();
        createMockFilm();

    }
    public void addMockUser(String login, String password){
        User user  = User.builder()
                .email(login)
                .login(login)
                .password(encoder.encode(password))
                .role(UserRole.USER)
                .is_blocked(false)
                .is_enabled(true)
                .build();

        userRepository.save(user);
    }
    public void addMockHeadAdmin(){
        User user  = User.builder()
                .login("head_admin")
                .password(encoder.encode("hadmin"))
                .role(UserRole.HEAD_ADMIN)
                .is_blocked(false)
                .is_enabled(true)
                .build();

        userRepository.save(user);
    }

    public void createMockFilm(){
        FilmStar actor = FilmStar.builder()
                .name("Joe")
                .lastname("Doe")
                .build();
        actor = filmStarRepository.save(actor);

        FilmStar director = FilmStar.builder()
                .name("Joe2")
                .lastname("Doe2")
                .build();
        director = filmStarRepository.save(director);

        CreateFilmRequest request = new CreateFilmRequest("film1",
                "description1",
                List.of(actor.getId()),
                List.of(director.getId()));

        filmService.createFilm(request);


    }
    @Test
    public void m(){

    }

    @Test
    public void AddRatingTest(){
        AddRatingRequest ratingRequest = new AddRatingRequest(1l,
                4, "description");
        String token = jwtService.generateToken(userRepository.getByLogin("user1@gmail.com"));
        token = "Bearer "+token;

        System.out.println("#################");
        System.out.println("Token: "+token);
        System.out.println("#################");

        ratingService.addRating(ratingRequest,token);

        Film film = filmService.getFilm(1l);

        assertEquals(1,film.getRatings().size());
        Float expected = 4f;
        assertEquals(expected, film.getRating());
    }

}
