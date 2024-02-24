package com.example.filmview.Rating;

import com.example.filmview.Rating.DTO.RatingDTO;
import com.example.filmview.Rating.DTO.RatingListDTO;

import java.util.List;

public class RatingMapper {
    public static RatingDTO mapRatingDTO(Rating rating){
        return new RatingDTO(rating.getId(),
                rating.getRated_film().getId(),
                rating.getRating_user().getLogin(),
                rating.getRating(),
                rating.getDescription());
    }


    public static RatingListDTO mapRatingListDTO(List<Rating> ratings){
        List<RatingDTO> result = ratings.stream().map(RatingMapper::mapRatingDTO).toList();

        return new RatingListDTO(result);
    }
}
