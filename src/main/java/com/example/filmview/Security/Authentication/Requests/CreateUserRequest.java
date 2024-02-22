package com.example.filmview.Security.Authentication.Requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


public record CreateUserRequest(

        @NotBlank(message = "Missing email") String email,
        @NotBlank(message = "Missing password") String password,
        @NotBlank(message = "Missing password confirmation") String password_repeat
){
}
