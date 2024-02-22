package com.example.filmview.User;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner addHeadAdmin(){
        return args -> {
            if(userService.getUserByUsername("head_admin") == null){
                userService.saveUser(User.builder()
                        .login("head_admin")
                        .password(passwordEncoder.encode("hadmin"))
                        .role(UserRole.HEAD_ADMIN)
                        .is_enabled(true)
                        .is_blocked(false)
                        .build());
            }
        };
    }
}
