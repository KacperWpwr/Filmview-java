package com.example.filmview.Rating;

import com.example.filmview.Rating.DTO.RatingListDTO;
import com.example.filmview.Rating.Requests.AddRatingRequest;

public interface IRatingService {
    RatingListDTO addRating(AddRatingRequest request, String token);
    boolean hasRated(Long film_id, String token);

}
