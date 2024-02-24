package com.example.filmview.Rating.Requests;

public record AddRatingRequest(long film_id, float rating, String description) {
}
