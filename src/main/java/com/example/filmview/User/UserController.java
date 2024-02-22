package com.example.filmview.User;


import com.example.filmview.Security.Authentication.DTO.RatedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/rated/{film_id}")
    public RatedDTO hasRated(Long film_id){
        return new RatedDTO(false);
    }

}
