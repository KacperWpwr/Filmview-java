package com.example.filmview.FilmStar;

import com.example.filmview.FilmStar.DTO.FilmStarDTO;
import com.example.filmview.FilmStar.DTO.FilmStarListDTO;
import com.example.filmview.FilmStar.Requests.CreateFilmStarRequest;

public interface IFilmStarService {
    FilmStar getFilmStarById(Long id);
    FilmStarDTO createFilmStar(CreateFilmStarRequest request);
    FilmStarListDTO getAllFilmStars();
    FilmStar saveFilmStar(FilmStar filmStar);
    FilmStarDTO getFilmStarDisplay(Long id);
}
