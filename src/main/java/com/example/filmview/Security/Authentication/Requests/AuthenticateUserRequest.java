package com.example.filmview.Security.Authentication.Requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record AuthenticateUserRequest(
        @NotBlank(message = "Missing login") String login,
        @NotBlank(message = "Missing password") String password) {
}
