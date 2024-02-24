package com.example.filmview.Security.Authentication;


import com.example.filmview.Security.Authentication.Requests.AuthenticateUserRequest;
import com.example.filmview.Security.Authentication.Requests.CreateAdminRequest;
import com.example.filmview.Security.Authentication.Requests.CreateUserRequest;
import com.example.filmview.Security.Authentication.Requests.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/user/register")
    public ResponseEntity<TokenDTO> registerUser(@RequestBody CreateUserRequest request){

        return new ResponseEntity<>(authenticationService.createUser(request),
                HttpStatusCode.valueOf(201));
    }

    @PostMapping("/user/login")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody AuthenticateUserRequest request){
        return new ResponseEntity<>(authenticationService.authenticateUser(request),
                HttpStatusCode.valueOf(200));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<TokenDTO> loginAdmin(@RequestBody AuthenticateUserRequest request){
        return new ResponseEntity<>(authenticationService.authenticateAdmin(request),
                HttpStatusCode.valueOf(200));
    }

    @PostMapping("/admin/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createAdmin(@RequestBody CreateAdminRequest request){
        authenticationService.createAdmin(request);
    }

}
