package com.example.filmview.Filmview.ApiTests;



import com.example.filmview.Filmview.Mapper.JSONMapper;
import com.example.filmview.Filmview.Mocks.Authentication.MockAuthenticateUserRequest;
import com.example.filmview.Filmview.Mocks.Authentication.MockCreateUserRequest;
import com.example.filmview.Filmview.Mocks.Authentication.MockTokenDTO;
import com.example.filmview.FilmviewApplication;
import com.example.filmview.Security.Authentication.Requests.TokenDTO;
import com.example.filmview.Security.JWT.JWTService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRepository;
import com.example.filmview.User.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
@AutoConfigureMockMvc

public class UserTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private JSONMapper jsonMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JWTService jwtService;

    @BeforeEach
    public void fillDatabase(){
        userRepository.deleteAll();

        System.out.println("Adding users");
        addMockUser("user1@gmail.com","12345");
        addMockUser("user2@gmail.com","12345");
        addMockUser("user3@gmail.com","12345");
        addMockUser("user4@gmail.com","12345");
        addMockAdmin("admin1","12345");
        addMockHeadAdmin();




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

    public void addMockAdmin(String login, String password){
        User user  = User.builder()
                .login(login)
                .password(encoder.encode(password))
                .role(UserRole.ADMIN)
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

    public MockTokenDTO registerUser(MockCreateUserRequest request){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/user/register")
                     .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(201,result.getResponse().getStatus());


            return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockTokenDTO.class);

        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }

    public void registerUserFail(MockCreateUserRequest request,Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/user/register")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(expectedCode,result.getResponse().getStatus());




        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
    }

    public MockTokenDTO authenticateUser(MockAuthenticateUserRequest request,Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/user/login")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(expectedCode,result.getResponse().getStatus());

            try{
                return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockTokenDTO.class);
            }catch(Exception e){
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            assert false;

        }
        return null;
    }

    public MockTokenDTO authenticateAdmin(MockAuthenticateUserRequest request,Integer expectedCode){
        try{
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/admin/login")
                    .content(jsonMapper.asJsonString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals(expectedCode,result.getResponse().getStatus());

            try{
                return jsonMapper.mapToObject(result.getResponse().getContentAsString(),MockTokenDTO.class);
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
    public void registerUserTest(){


//        MockCreateUserRequest createUserRequest = new MockCreateUserRequest("12345@gmail.com","12345","12345");
        MockCreateUserRequest createUserRequest =   MockCreateUserRequest.builder()
                .email("jondoe@gmail.com")
                .password("12345")
                .password_repeat("12345")
                .build();

        registerUser(createUserRequest);

    }

    @Test
    public void registerUserTestPasswordMissmatch(){
        MockCreateUserRequest createUserRequest =   MockCreateUserRequest.builder()
                .email("jondoe@gmail.com")
                .password("12345")
                .password_repeat("1234")
                .build();

        registerUserFail(createUserRequest,409);
    }

    @Test
    public void registerUserTestEmailTaken(){
        MockCreateUserRequest createUserRequest =   MockCreateUserRequest.builder()
                .email("user1@gmail.com")
                .password("12345")
                .password_repeat("12345")
                .build();

        registerUserFail(createUserRequest,410);
    }

    @Test
    public void authenticateUserTest(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("user1@gmail.com")
                .password("12345")
                .build();

        authenticateUser(authenticateUserRequest,200);
    }

    @Test
    public void authenticateUserTestNotFound(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("user5@gmail.com")
                .password("12345")
                .build();

        authenticateUser(authenticateUserRequest,404);
    }

    @Test
    public void authenticateUserTestWrongPassword(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("user1@gmail.com")
                .password("1234")
                .build();

        authenticateUser(authenticateUserRequest,403);
    }

    @Test
    public void authenticateUserTestWrongAuthorities(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("admin1")
                .password("12345")
                .build();

        authenticateUser(authenticateUserRequest,404);
    }

    @Test
    public void authenticateAdmin(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("admin1")
                .password("12345")
                .build();

        MockTokenDTO token = authenticateAdmin(authenticateUserRequest,200);

        Claims claims = jwtService.extractAllClaims(token.getToken());
        assertTrue(claims.get("roles").toString().contains("ADMIN"));
    }

    @Test
    public void authenticateHeadAdmin(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("head_admin")
                .password("hadmin")
                .build();

        MockTokenDTO token = authenticateAdmin(authenticateUserRequest,200);

        Claims claims = jwtService.extractAllClaims(token.getToken());
        assertTrue(claims.get("roles").toString().contains("HEAD_ADMIN"));
    }

    @Test
    public void authenticateAdminWrongAuthorities(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("user1")
                .password("12345")
                .build();

        authenticateAdmin(authenticateUserRequest,404);

    }

    @Test
    public void authenticateAdminPasswordMismatch(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("admin1")
                .password("54321")
                .build();

        authenticateAdmin(authenticateUserRequest,403);
    }

    @Test
    public void authenticateAdminNotFound(){
        MockAuthenticateUserRequest authenticateUserRequest = MockAuthenticateUserRequest.builder()
                .login("admin_none")
                .password("12345")
                .build();

        authenticateAdmin(authenticateUserRequest,404);
    }


}
