package com.example.filmview.Rating;

import com.example.filmview.Application.ApplicationException;
import com.example.filmview.Film.Film;
import com.example.filmview.Film.FilmService;
import com.example.filmview.Film.IFilmService;
import com.example.filmview.Rating.DTO.RatingListDTO;
import com.example.filmview.Rating.Requests.AddRatingRequest;
import com.example.filmview.Security.JWT.IJWTService;
import com.example.filmview.Security.JWT.JWTService;
import com.example.filmview.User.IUserService;
import com.example.filmview.User.User;
import com.example.filmview.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService implements IRatingService{
    private final RatingRepository ratingRepository;
    private final IFilmService filmService;
    private final IUserService userService;
    private final IJWTService jwtService;


    @Override
    public RatingListDTO addRating(AddRatingRequest request, String token){

        String username = jwtService.extractUsername(token.substring(7));

        Film film = filmService.getFilm(request.film_id());
        if( film== null){
            throw new ApplicationException("Film not found",404);
        }

        User user = userService.getUserByUsername(username);
        if( user== null){
            throw new ApplicationException("User not found",404);
        }
        if(ratingRepository.hasRated(username, request.film_id())){
            throw new ApplicationException("Film already rated",401);
        }

        Rating rating = Rating.builder()
                .rating_user(user)
                .rated_film(film)
                .rating(request.rating())
                .description(request.description())
                .build();

        rating = ratingRepository.save(rating);

        film.addRating(rating);

        film = filmService.saveFilm(film);

        return RatingMapper.mapRatingListDTO(film.getRatings());
    }

    @Override
    public boolean hasRated(Long film_id, String token){
        String username = jwtService.extractUsername(token.substring(7));
        System.out.println("Username: "+username);
        System.out.println("Film: "+film_id);
        return ratingRepository.hasRated(username, film_id);
    }


}
