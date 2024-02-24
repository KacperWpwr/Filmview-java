package com.example.filmview.Film;

import com.example.filmview.Actor.Actor;
import com.example.filmview.Actor.ActorId;
import com.example.filmview.Actor.ActorService;
import com.example.filmview.Actor.IActorService;
import com.example.filmview.Application.ApplicationException;
import com.example.filmview.Director.Director;
import com.example.filmview.Director.DirectorId;
import com.example.filmview.Director.DirectorService;
import com.example.filmview.Director.IDirectorService;
import com.example.filmview.Film.DTO.FilmListDTO;
import com.example.filmview.Film.DTO.FilmPageDTO;
import com.example.filmview.Film.DTO.FilmPreviewDTO;
import com.example.filmview.Film.Requests.CreateFilmRequest;
import com.example.filmview.FilmStar.DTO.FilmStarDTO;
import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarService;
import com.example.filmview.FilmStar.IFilmStarService;
import com.example.filmview.Image.IImageService;
import com.example.filmview.Image.ImageService;
import com.example.filmview.Rating.DTO.RatingListDTO;
import com.example.filmview.Rating.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;
    private final IImageService imageService;
    private final IFilmStarService filmStarService;
    private final IActorService actorService;
    private final IDirectorService directorService;

    @Override
    public FilmPageDTO createFilm(CreateFilmRequest request) {

        if(filmRepository.getFilmByTitle(request.title()) != null){
            throw new ApplicationException("Film with this title already exists",410);
        }

        if(request.actors().isEmpty()){
            throw new ApplicationException("Film has to have at least one actor",412);
        }
        if(request.directors().isEmpty()){
            throw new ApplicationException("Film has to have at least one director",412);
        }

        for(Long id : request.actors()){
            if(filmStarService.getFilmStarById(id) == null){
                throw new ApplicationException("Actor id="+id+" doesn't exist",409);
            }
        }

        for(Long id : request.directors()){
            if(filmStarService.getFilmStarById(id) == null){
                throw new ApplicationException("Director id="+id+" doesn't exist",409);
            }
        }

        Film new_film = Film.builder()
                .title(request.title())
                .description(request.description())
                .build();

        new_film = filmRepository.save(new_film);


       if( request.actors().stream().distinct().toList().size() != request.actors().size()){
           throw new ApplicationException("Actors are not unique",411);
       }
        if( request.directors().stream().distinct().toList().size() != request.directors().size()){
            throw new ApplicationException("Directors are not unique",411);
        }

        List<Actor> actors = new ArrayList<>();

        for (Long id : request.actors()){
            FilmStar filmStar = filmStarService.getFilmStarById(id);
            Actor actor = new Actor(new ActorId(id, new_film.getId()),new_film, filmStar);
            actor = actorService.saveActor(actor);
            actors.add(actor);
            filmStar.addStarredFilm(actor);
            filmStarService.saveFilmStar(filmStar);
        }

        List<Director> directors = new ArrayList<>();

        for (Long id : request.actors()){
            FilmStar  filmStar = filmStarService.getFilmStarById(id);
            Director director = new Director(new DirectorId(id, new_film.getId()),new_film, filmStar);
            director = directorService.saveDirector(director);
            directors.add(director);
            filmStar.addDirectedFilm(director);
            filmStarService.saveFilmStar(filmStar);
        }

        new_film.setActors(actors);
        new_film.setDirectors(directors);

        new_film = filmRepository.save(new_film);

        List<FilmStarDTO> actorsDTO = new_film.getActors()
                .stream().map(actor -> new FilmStarDTO(actor.getFilmStar().getId(),
                        actor.getFilmStar().getName(),
                        actor.getFilmStar().getLastname())).toList();

        List<FilmStarDTO> directorsDTO= new_film.getDirectors().stream()
                .map(director -> new FilmStarDTO(director.getFilmStar().getId(),
                        director.getFilmStar().getName(),
                        director.getFilmStar().getLastname())).toList();

        return new FilmPageDTO(new_film.getId(),new_film.getTitle(),
                new_film.getDescription(), new_film.getRating(),
                actorsDTO, directorsDTO);
    }

    @Override
    public FilmPageDTO getFilmPage(Long id) {
        Film film = filmRepository.getFilmById(id);
        if(film == null){
            throw new ApplicationException("Film not found",404);
        }

        List<FilmStarDTO> actors =film.getActors().stream()
                .map(actor -> new FilmStarDTO(actor.getFilmStar().getId(),
                        actor.getFilmStar().getName(),
                        actor.getFilmStar().getLastname())).toList();


        List<FilmStarDTO> direcors =film.getDirectors().stream()
                .map(director -> new FilmStarDTO(director.getFilmStar().getId(),
                        director.getFilmStar().getName(),
                        director.getFilmStar().getLastname())).toList();

        return new FilmPageDTO(id, film.getTitle(),
                film.getDescription(), film.getRating(),actors,direcors );
    }


    @Override
    public FilmPreviewDTO getFilmPreview(Long id){
        Film film = filmRepository.getFilmById(id);

        if(film == null){
            throw new ApplicationException("Film not found",404);
        }

        return new FilmPreviewDTO(film.getTitle(), film.getId(), film.getRating());
    }

    @Override
    public FilmListDTO getTopFilms(int quantity){
        List<Film> films = filmRepository.findAll();

        if (films.isEmpty()){
            return new FilmListDTO(new ArrayList<>());
        }

        Collections.sort(films, new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getRating().compareTo(o2.getRating());
            }
        });

        List<Film> temp = films.subList(0, Math.min(quantity, films.size()));


        List<FilmPreviewDTO> output = temp.stream().map(film -> new FilmPreviewDTO(film.getTitle(), film.getId(), film.getRating())).toList();

        return new FilmListDTO(output);
    }

    @Override
    public FilmListDTO getAllFilms() {
        List<Film> films = filmRepository.findAll();

        List<FilmPreviewDTO> output = films.stream().map(film -> new FilmPreviewDTO(film.getTitle(), film.getId(), film.getRating())).toList();

        return new FilmListDTO(output);
    }

    @Override
    public Film getFilm(long id){
        return filmRepository.getFilmById(id);
    }

    @Override
    public Film saveFilm(Film film){
        return filmRepository.save(film);
    }

    @Override
    public RatingListDTO getFilmRatings(Long id) {
        Film film = filmRepository.getFilmById(id);
        if(film == null){
            throw new ApplicationException("Film not found", 404);
        }

        return RatingMapper.mapRatingListDTO(film.getRatings());
    }
}
