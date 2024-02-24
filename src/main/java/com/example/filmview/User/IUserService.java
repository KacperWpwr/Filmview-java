package com.example.filmview.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User getUserByUsername(String username);
    User saveUser(User user);
}
