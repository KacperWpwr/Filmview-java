package com.example.filmview.Filmview.Mocks.Authentication;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MockAuthenticateUserRequest {
    private String login;
    private String password;
}
