package com.example.filmview.Film.DTO;

import com.example.filmview.FilmStar.DTO.FilmStarDTO;

import java.util.List;

public record FilmPageDTO(Long id, String title, String description, Float rating, List<FilmStarDTO> actors, List<FilmStarDTO> directors) {
}
