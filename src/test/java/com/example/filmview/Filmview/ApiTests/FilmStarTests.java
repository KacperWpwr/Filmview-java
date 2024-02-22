package com.example.filmview.Filmview.ApiTests;

import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarRepository;
import com.example.filmview.Filmview.Mapper.JSONMapper;
import com.example.filmview.Filmview.Mocks.Authentication.MockAuthenticateUserRequest;
import com.example.filmview.Filmview.Mocks.Authentication.MockTokenDTO;
import com.example.filmview.Filmview.Mocks.FilmStar.MockCreateFilmStarRequest;
import com.example.filmview.Filmview.Mocks.FilmStar.MockFilmStarDTO;
import com.example.filmview.FilmviewApplication;
import com.example.filmview.Security.Authentication.Requests.TokenDTO;
import com.example.filmview.Security.JWT.JWTService;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
@AutoConfigureMockMvc
public class FilmStarTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilmStarRepository filmStarRepository;
    @Autowired
    private JSONMapper jsonMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JWTService jwtService;

    @BeforeEach
    public void fillDatabase(){
        userRepository.deleteAll();
        filmStarRepository.deleteAll();

        System.out.println("Adding users");
        addMockHeadAdmin();
        addMockUser("user1","12345");
        addMockFilmStar("Joe","Doe");
        addMockFilmStar("Mock","Star");
        addMockFilmStar("Test","Star");
    }

    public MockTokenDTO authenticateUser(MockAuthenticateUserRequest request){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/user/login")
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


    public void addMockFilmStar(String name, String lastName){
        FilmStar star = FilmStar.builder()
                .name(name)
                .lastname(lastName)
                .build();

        filmStarRepository.save(star);
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

    public MockFilmStarDTO addFilmStar(MockCreateFilmStarRequest request,String token,Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/filmstar/create")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer "+token)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(Optional.ofNullable(expectedCode),result.getResponse().getStatus());


            try{
                return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockFilmStarDTO.class);
            }catch (Exception e){
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }

    @Test
    public void addFilmStar(){
        MockCreateFilmStarRequest request =
                MockCreateFilmStarRequest.builder()
                        .name("Added")
                        .lastname("Star")
                        .build();


        MockTokenDTO admin_token = authenticateHeadAdmin();

        MockFilmStarDTO filmStarDTO = addFilmStar(request,admin_token.getToken(),201);

        assertEquals("Added",filmStarDTO.getName());
        assertEquals("Star",filmStarDTO.getLastname());
    }

    @Test
    public void addFilmStarNotEnoughAuthorities(){
        MockCreateFilmStarRequest request =
                MockCreateFilmStarRequest.builder()
                        .name("Added")
                        .lastname("Star")
                        .build();


        MockTokenDTO admin_token = authenticateUser(new MockAuthenticateUserRequest("user1","12345"));

        addFilmStar(request,admin_token.getToken(),403);


    }
}
