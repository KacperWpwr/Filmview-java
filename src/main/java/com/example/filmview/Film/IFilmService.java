package com.example.filmview.Film;

import com.example.filmview.Film.DTO.FilmListDTO;
import com.example.filmview.Film.DTO.FilmPageDTO;
import com.example.filmview.Film.DTO.FilmPreviewDTO;
import com.example.filmview.Film.Requests.CreateFilmRequest;
import com.example.filmview.Rating.DTO.RatingListDTO;

public interface IFilmService {
    FilmPageDTO createFilm(CreateFilmRequest request);
    FilmPageDTO getFilmPage(Long id);
    FilmPreviewDTO getFilmPreview(Long id);
    FilmListDTO getTopFilms(int quantity);
    FilmListDTO getAllFilms();
    Film getFilm(long id);
    Film saveFilm(Film film);
    RatingListDTO getFilmRatings(Long id);

}
