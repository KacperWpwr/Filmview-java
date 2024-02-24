package com.example.filmview.Rating;


import com.example.filmview.Rating.DTO.RatingListDTO;
import com.example.filmview.Rating.Requests.AddRatingRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final IRatingService ratingService;
    @PostMapping("/add")
    public ResponseEntity<RatingListDTO> AddRating(@RequestBody AddRatingRequest request , @RequestHeader (name="Authorization") String token){
       return new ResponseEntity<>( ratingService.addRating(request, token), HttpStatusCode.valueOf(200));
    }
}
