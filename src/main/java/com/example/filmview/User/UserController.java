package com.example.filmview.User;


import com.example.filmview.Rating.IRatingService;
import com.example.filmview.Rating.DTO.RatedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final IRatingService ratingService;
    @GetMapping("/rated/{film_id}")
    public RatedDTO hasRated(@PathVariable Long film_id, @RequestHeader(name="Authorization") String token){
        return new RatedDTO(ratingService.hasRated(film_id,token));
    }

}
