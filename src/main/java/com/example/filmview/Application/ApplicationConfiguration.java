package com.example.filmview.Application;

import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarRepository;
import com.example.filmview.User.User;
import com.example.filmview.User.UserRepository;
import com.example.filmview.User.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    private final FilmStarRepository filmStarRepository;
    private final PasswordEncoder encoder;
    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
            User user  = User.builder()
                    .login("head_admin")
                    .password(encoder.encode("hadmin"))
                    .role(UserRole.HEAD_ADMIN)
                    .is_blocked(false)
                    .is_enabled(true)
                    .build();

            userRepository.save(user);

            FilmStar star = FilmStar.builder()
                    .name("Joe")
                    .lastname("Doe")
                    .build();

            star = filmStarRepository.save(star);
        };
    }

}
