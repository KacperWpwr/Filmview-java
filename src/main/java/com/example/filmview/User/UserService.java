package com.example.filmview.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        return getUserByUsername(username);
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.getByLogin(username);
    }

    @Override
    public User saveUser(User user){

        return userRepository.save(user);
    }

}
