package com.example.filmview.Security.Authentication;

import com.example.filmview.Security.Authentication.Requests.AuthenticateUserRequest;
import com.example.filmview.Security.Authentication.Requests.CreateAdminRequest;
import com.example.filmview.Security.Authentication.Requests.CreateUserRequest;
import com.example.filmview.Security.Authentication.DTO.TokenDTO;

public interface IAuthenticationService {
    TokenDTO createUser(CreateUserRequest request);
    TokenDTO authenticateUser(AuthenticateUserRequest request);
    TokenDTO authenticateAdmin(AuthenticateUserRequest request);
    void createAdmin(CreateAdminRequest request);
}
