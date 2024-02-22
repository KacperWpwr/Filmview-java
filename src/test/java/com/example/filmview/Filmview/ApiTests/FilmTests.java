package com.example.filmview.Filmview.ApiTests;

import com.example.filmview.Actor.Actor;
import com.example.filmview.Actor.ActorId;
import com.example.filmview.Actor.ActorRepository;
import com.example.filmview.Director.Director;
import com.example.filmview.Director.DirectorId;
import com.example.filmview.Director.DirectorRepository;
import com.example.filmview.Film.Film;
import com.example.filmview.Film.FilmRepository;
import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarRepository;
import com.example.filmview.Filmview.Mapper.JSONMapper;
import com.example.filmview.Filmview.Mocks.Authentication.MockAuthenticateUserRequest;
import com.example.filmview.Filmview.Mocks.Authentication.MockTokenDTO;
import com.example.filmview.Filmview.Mocks.Film.MockCreateFilmRequest;
import com.example.filmview.Filmview.Mocks.Film.MockFilmPageDTO;
import com.example.filmview.FilmviewApplication;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRepository;
import com.example.filmview.User.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
@AutoConfigureMockMvc
public class FilmTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilmStarRepository filmStarRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private JSONMapper jsonMapper;
    @Autowired
    private PasswordEncoder encoder;


    private List<FilmStar> testFilmStars = null;


    private Film testFilm = null;

    @BeforeEach
    public void fillDatabase(){
        userRepository.deleteAll();
        actorRepository.deleteAll();
        directorRepository.deleteAll();
        filmStarRepository.deleteAll();
        filmRepository.deleteAll();

        System.out.println("Adding users");
        addMockHeadAdmin();
        testFilmStars = new ArrayList<>();
        testFilmStars.add(addMockFilmStar("Joe","Doe"));
        testFilmStars.add(addMockFilmStar("Mock","Star"));
        testFilmStars.add(addMockFilmStar("Test","Star"));

        testFilm = addMockFilm("Mock Film","Mock Film Description",
                List.of(testFilmStars.get(0)),
                List.of(testFilmStars.get(1)));
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


    public FilmStar addMockFilmStar(String name, String lastName){
        FilmStar star = FilmStar.builder()
                .name(name)
                .lastname(lastName)
                .build();

        return filmStarRepository.save(star);
    }

    public Film addMockFilm(String title, String description, List<FilmStar> actors, List<FilmStar> directors){
        Film film = Film.builder()
                .title(title)
                .description(description)
                .build();

        film = filmRepository.save(film);

        List<Actor> newActors = new ArrayList<>();

        for (FilmStar filmStar: actors){
            Actor actor = new Actor(new ActorId(film.getId(),filmStar.getId()),
                    film,filmStar);

            actor = actorRepository.save(actor);

            newActors.add(actor);
        }

        List<Director> newDirectors = new ArrayList<>();

        for (FilmStar filmStar: directors){
            Director director = new Director(new DirectorId(film.getId(),filmStar.getId()),
                    film,filmStar);

            director = directorRepository.save(director);

            newDirectors.add(director);
        }

        film.setDirectors(newDirectors);
        film.setActors(newActors);


        return film;


    }

    public Actor createActor(FilmStar filmStar, Film film){

        Actor actor = new Actor(new ActorId(filmStar.getId(), film.getId()),film,filmStar);
        return actorRepository.save(actor);
    }

    public Director createDirector(FilmStar filmStar, Film film){
        Director director = new Director(new DirectorId(filmStar.getId(), film.getId()),film,filmStar);
        return directorRepository.save(director);
    }

    public MockTokenDTO authenticateHeadAdmin(){
        MockAuthenticateUserRequest request = MockAuthenticateUserRequest.builder()
                .login("head_admin")
                .password("hadmin")
                .build();
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/admin/login")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(200,result.getResponse().getStatus());


            return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockTokenDTO.class);

        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }

    public MockFilmPageDTO createFilm(MockCreateFilmRequest request,String token, Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/film/create")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer "+token)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(expectedCode,result.getResponse().getStatus());


            try{
                return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockFilmPageDTO.class);
            }catch(Exception e){
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }

    public MockFilmPageDTO getFilmById(Long id, Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/film/"+id+"/display")
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(expectedCode,result.getResponse().getStatus());


            try{
                return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockFilmPageDTO.class);
            }catch(Exception e){
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }


    @Test
    public void getFilmPageTest(){
        MockFilmPageDTO filmPageDTO = getFilmById(testFilm.getId(),200);

        assertEquals(testFilm.getTitle() , filmPageDTO.getTitle());
        assertEquals(testFilm.getDescription() , filmPageDTO.getDescription());
    }

    @Test
    public void getFilmPageTestNotFound(){
        MockFilmPageDTO filmPageDTO = getFilmById(testFilm.getId()+10l,404);
    }

    @Test
    public void createFilmTest(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId()))
                .directors(List.of(testFilmStars.get(2).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        MockFilmPageDTO filmPageDTO = createFilm(createFilmRequest, token.getToken(),201);


        assertEquals("Test Film", filmPageDTO.getTitle());
        assertEquals("Test description", filmPageDTO.getDescription());

    }

    @Test
    public void createFilmTestTitleTaken(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title(testFilm.getTitle())
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId()))
                .directors(List.of(testFilmStars.get(2).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),410);

    }

    @Test
    public void createFilmTestEmptyActors(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of())
                .directors(List.of(testFilmStars.get(2).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),412);

    }

    @Test
    public void createFilmTestEmptyDirectors(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId()))
                .directors(List.of())
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),412);

    }

    @Test
    public void createFilmTestDirectorsNotUnique(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId()))
                .directors(List.of(testFilmStars.get(1).getId(),testFilmStars.get(1).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),411);

    }

    @Test
    public void createFilmTestActorsNotUnique(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(0).getId()))
                .directors(List.of(testFilmStars.get(1).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),411);

    }

    @Test
    public void createFilmTestActorNotExist(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId(),100l))
                .directors(List.of(testFilmStars.get(1).getId()))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),409);

    }

    @Test
    public void createFilmTestDirectorNotExist(){
        MockCreateFilmRequest createFilmRequest = MockCreateFilmRequest.builder()
                .title("Test Film")
                .description("Test description")
                .actors(List.of(testFilmStars.get(0).getId(),testFilmStars.get(1).getId()))
                .directors(List.of(testFilmStars.get(1).getId(),100l))
                .build();

        MockTokenDTO token = authenticateHeadAdmin();

        createFilm(createFilmRequest, token.getToken(),409);

    }
}
