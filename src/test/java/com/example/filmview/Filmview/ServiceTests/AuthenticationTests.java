package com.example.filmview.Filmview.ServiceTests;

import com.example.filmview.Application.ApplicationException;
import com.example.filmview.Security.Authentication.AuthenticationService;
import com.example.filmview.Security.Authentication.Requests.AuthenticateUserRequest;
import com.example.filmview.Security.Authentication.Requests.CreateAdminRequest;
import com.example.filmview.Security.Authentication.Requests.CreateUserRequest;
import com.example.filmview.Security.Authentication.DTO.TokenDTO;
import com.example.filmview.Security.JWT.IJWTService;
import com.example.filmview.User.IUserService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTests {
    @Mock
    private IJWTService jwtService ;
    @Mock
    private IUserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    public void CreateUser_Success(){
        CreateUserRequest request = new CreateUserRequest("joedoe@gmail.com","12345","12345");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();

        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("token");
        when(passwordEncoder.encode("12345")).thenReturn("54321");
        when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);

        TokenDTO dto = authenticationService.createUser(request);
        assertThat(dto).isNotNull();
        assertEquals("token",dto.token());
    }
    @Test
    public void CreateUser_PasswordMissmatch_409(){
        CreateUserRequest request = new CreateUserRequest("joedoe@gmail.com","12345","123456");


        try{
            TokenDTO dto = authenticationService.createUser(request);
            assert false;
        }catch(ApplicationException e){
            assertEquals(409,e.getCode());
            assertEquals("Password Mismatch",e.getMessage());
        }

    }

    @Test
    public void CreateUser_EmailTaken_410(){
        CreateUserRequest request = new CreateUserRequest("joedoe@gmail.com","12345","12345");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("joedoe@gmail.com")).thenReturn(user);

        try{
            TokenDTO dto = authenticationService.createUser(request);
            assert false;
        }catch(ApplicationException e){
            assertEquals(410,e.getCode());
            assertEquals("Email Taken",e.getMessage());
        }

    }

    @Test
    public void AuthenticateUser_Success(){
        AuthenticateUserRequest request = new AuthenticateUserRequest(
                "joedoe@gmail.com","12345");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("joedoe@gmail.com")).thenReturn(user);
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("token");
        when(passwordEncoder.matches("12345","54321")).thenReturn(true);

        TokenDTO dto = authenticationService.authenticateUser(request);
        assertThat(dto).isNotNull();
        assertEquals("token",dto.token());
    }

    @Test
    public void AuthenticateUser_NotFound_404(){
        AuthenticateUserRequest request = new AuthenticateUserRequest(
                "joedoe@gmail.com","12345");

        try{
            TokenDTO dto = authenticationService.authenticateUser(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("User not found",e.getMessage());
        }
    }

    @Test
    public void AuthenticateUser_WrongAuthoritiesAdmin_404(){
        AuthenticateUserRequest request = new AuthenticateUserRequest(
                "joedoe@gmail.com","12345");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("joedoe@gmail.com")).thenReturn(user);


        try{
            TokenDTO dto = authenticationService.authenticateUser(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Wrong authorities",e.getMessage());
        }
    }
    @Test
    public void AuthenticateUser_WrongAuthoritiesHeadAdmin_404(){
        AuthenticateUserRequest request = new AuthenticateUserRequest(
                "joedoe@gmail.com","12345");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.HEAD_ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("joedoe@gmail.com")).thenReturn(user);

        try{
            TokenDTO dto = authenticationService.authenticateUser(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Wrong authorities",e.getMessage());
        }
    }

    @Test
    public void AuthenticateUser_InvalidPassword_403(){
        AuthenticateUserRequest request = new AuthenticateUserRequest(
                "joedoe@gmail.com","123456");
        User user = User.builder()
                .email("joedoe@gmail.com")
                .login("joedoe@gmail.com")
                .password("54321")
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("joedoe@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("123456","54321")).thenReturn(false);

        try{
            TokenDTO dto = authenticationService.authenticateUser(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(403,e.getCode());
            assertEquals("Invalid password",e.getMessage());
        }
    }


    @Test
    public void CreateAdmin_Success(){
        CreateAdminRequest request = new CreateAdminRequest("admin","12345");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(passwordEncoder.encode("12345")).thenReturn("54321");
        when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);

        authenticationService.createAdmin(request);

    }

    @Test
    public void CreateAdmin_LoginTaken_410(){
        CreateAdminRequest request = new CreateAdminRequest("admin","12345");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("admin")).thenReturn(user);

        try{
            authenticationService.createAdmin(request);
            assert false;
        }catch(ApplicationException e){
            assertEquals(410, e.getCode());
            assertEquals("Login taken",e.getMessage());
        }

    }

    @Test
    public void AuthenticateAdmin_Admin_Success(){
        AuthenticateUserRequest request = new AuthenticateUserRequest("admin","12345");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("12345","54321")).thenReturn(true);
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("token");


        TokenDTO dto = authenticationService.authenticateAdmin(request);

        assertThat(dto).isNotNull();
        assertEquals("token",dto.token());
    }
    @Test
    public void AuthenticateAdmin_HeadAdmin_Success(){
        AuthenticateUserRequest request = new AuthenticateUserRequest("admin","12345");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.HEAD_ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("12345","54321")).thenReturn(true);
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn("token");


        TokenDTO dto = authenticationService.authenticateAdmin(request);

        assertThat(dto).isNotNull();
        assertEquals("token",dto.token());
    }

    @Test
    public void AuthenticateAdmin_User_NotAnAdmin_404(){
        AuthenticateUserRequest request = new AuthenticateUserRequest("admin","12345");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("admin")).thenReturn(user);



        try{
            TokenDTO dto = authenticationService.authenticateAdmin(request);
            assert false;
        }catch(ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Admin not found",e.getMessage());
        }


    }

    @Test
    public void AuthenticateAdmin_AdminNotFound_404(){
        AuthenticateUserRequest request = new AuthenticateUserRequest("admin","12345");

        try{
            TokenDTO dto = authenticationService.authenticateAdmin(request);
            assert false;
        }catch(ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Admin not found",e.getMessage());
        }
    }

    @Test
    public void AuthenticateAdmin_PasswordMismatch_403(){
        AuthenticateUserRequest request = new AuthenticateUserRequest("admin","123456");
        User user = User.builder()
                .login("admin")
                .password("54321")
                .role(UserRole.HEAD_ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();
        when(userService.getUserByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("123456","54321")).thenReturn(false);



        try{
            TokenDTO dto = authenticationService.authenticateAdmin(request);
        }catch(ApplicationException e){
            assertEquals(403,e.getCode());
            assertEquals("Invalid password",e.getMessage());
        }


    }




}
