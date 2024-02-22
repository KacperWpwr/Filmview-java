package com.example.filmview.Filmview.ServiceTests;

import com.example.filmview.Application.ApplicationException;
import com.example.filmview.FilmviewApplication;
import com.example.filmview.Security.Authentication.AuthenticationService;
import com.example.filmview.Security.Authentication.Requests.CreateAdminRequest;
import com.example.filmview.Security.Authentication.Requests.CreateUserRequest;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmviewApplication.class
)
public class AuthenticationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationService authenticationService;

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

    @Test
    public void RegisterUserTest(){
        CreateUserRequest request = new CreateUserRequest("user@gmail.com","12345","12345");

        authenticationService.createUser(request);

        assertNotNull(userRepository.getByLogin("user@gmail.com"));
    }

    @Test
    public void RegisterUserTestMailTaken(){
        CreateUserRequest request = new CreateUserRequest("user1@gmail.com","12345","12345");

        try{
            authenticationService.createUser(request);
            assert false;

        }catch(ApplicationException e){

        }
    }

    @Test
    public void RegisterUserTestPasswordMismatch(){
        CreateUserRequest request = new CreateUserRequest("user@gmail.com","12345","123456");

        try{
            authenticationService.createUser(request);
            assert false;

        }catch(ApplicationException e){

        }
    }

    @Test
    public void CreateAdminTest(){
        CreateAdminRequest request = new CreateAdminRequest("admin2","123456");
        authenticationService.createAdmin(request);

        assertNotNull(userRepository.getByLogin("admin2"));

    }




}
