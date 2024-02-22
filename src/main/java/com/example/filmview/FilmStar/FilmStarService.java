package com.example.filmview.FilmStar;

import com.example.filmview.Application.ApplicationException;
import com.example.filmview.FilmStar.DTO.FilmStarDTO;
import com.example.filmview.FilmStar.DTO.FilmStarListDTO;
import com.example.filmview.FilmStar.Requests.CreateFilmStarRequest;
import com.example.filmview.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmStarService {
    private final FilmStarRepository filmStarRepository;
    private final ImageService imageService;


    public FilmStar getFilmStarById(Long id){
        return filmStarRepository.getFilmStarById(id);
    }

    public FilmStarDTO createFilmStar(CreateFilmStarRequest request) {
        FilmStar newFilmStar = FilmStar.builder()
                .name(request.name())
                .lastname(request.lastname())
                .build();

        newFilmStar = filmStarRepository.save(newFilmStar);


        return new FilmStarDTO(newFilmStar.getId()
                ,newFilmStar.getName()
                ,newFilmStar.getLastname());

    }

    public FilmStarListDTO getAllFilmStars(){
        List<FilmStar> stars = filmStarRepository.findAll();


        List<FilmStarDTO> starsDTOs = stars.stream().map(s->
                new FilmStarDTO(s.getId(),s.getName(), s.getLastname())).toList();

        return new FilmStarListDTO(starsDTOs);
    }

    public FilmStar saveFilmStar(FilmStar filmStar){
        return filmStarRepository.save(filmStar);
    }

    public FilmStarDTO getFilmStarDisplay(Long id) {
        FilmStar star = filmStarRepository.getFilmStarById(id);

        if(star == null){
            throw new ApplicationException("Film Star not found",404);
        }

        return new FilmStarDTO(star.getId(),  star.getName(),star.getLastname());
    }
}
