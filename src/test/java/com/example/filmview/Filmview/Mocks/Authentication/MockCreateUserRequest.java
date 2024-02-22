package com.example.filmview.Filmview.Mocks.Authentication;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Builder
public class MockCreateUserRequest {
    private String email;
    private String password;
    private String password_repeat;
}
