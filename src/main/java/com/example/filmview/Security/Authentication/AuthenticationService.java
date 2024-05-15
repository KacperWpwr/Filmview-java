package com.example.filmview.Security.Authentication;


import com.example.filmview.Application.ApplicationException;
import com.example.filmview.Security.Authentication.Requests.AuthenticateUserRequest;
import com.example.filmview.Security.Authentication.Requests.CreateAdminRequest;
import com.example.filmview.Security.Authentication.Requests.CreateUserRequest;
import com.example.filmview.Security.Authentication.DTO.TokenDTO;
import com.example.filmview.Security.JWT.IJWTService;
import com.example.filmview.User.IUserService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements IAuthenticationService{
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IJWTService jwtService;

    public TokenDTO createUser(CreateUserRequest request) {

        User check = userService.getUserByUsername(request.email());
        if(check != null){
            throw new ApplicationException("Email Taken",410);
        }
        if(!request.password().equals(request.password_repeat()) ){
            throw new ApplicationException("Password Mismatch",409);
        }

        User user = User.builder()
                .login(request.email())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.USER)
                .is_enabled(true)
                .is_blocked(false)
                .build();

        user = userService.saveUser(user);

        String token = jwtService.generateToken(user);

        return new TokenDTO(token);
    }

    public TokenDTO authenticateUser(AuthenticateUserRequest request){


        User user = userService.getUserByUsername(request.login());


        if(user == null){
            throw new ApplicationException("User not found",404);
        }

        Boolean containsAuthority = false;

        for (GrantedAuthority auth : user.getAuthorities()){
            if(auth.getAuthority().equals("USER") ){
                containsAuthority = true;
                break;
            }
        }

        if(!containsAuthority){
            throw new ApplicationException("Wrong authorities",404);
        }

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new ApplicationException("Invalid password",403);
        }

        String token = jwtService.generateToken(user);

        return new TokenDTO(token);
    }

    public TokenDTO authenticateAdmin(AuthenticateUserRequest request){
        User user = userService.getUserByUsername(request.login());

        if(user== null){
            throw new ApplicationException("Admin not found",404);
        }

        Boolean containsAuthority = false;

        for (GrantedAuthority auth : user.getAuthorities()){
            if(auth.getAuthority().equals("ADMIN") || auth.getAuthority().equals("HEAD_ADMIN")){
                containsAuthority = true;
                break;
            }
        }

        if(!containsAuthority){
            throw new ApplicationException("Admin not found",404);
        }

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new ApplicationException("Invalid password",403);
        }

        String token = jwtService.generateToken(user);

        return new TokenDTO(token);
    }

    public void createAdmin(CreateAdminRequest request) {

        if(userService.getUserByUsername(request.login()) !=null){
            throw new ApplicationException("Login taken",410);
        }
        User user = User.builder()
                .login(request.login())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.ADMIN)
                .is_enabled(true)
                .is_blocked(false)
                .build();

        userService.saveUser(user);

    }
}
