package com.example.filmview.Filmview.ServiceTests;

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
import com.example.filmview.Film.Film;
import com.example.filmview.Film.FilmRepository;
import com.example.filmview.Film.FilmService;
import com.example.filmview.Film.Requests.CreateFilmRequest;
import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarService;
import com.example.filmview.FilmStar.IFilmStarService;
import com.example.filmview.Rating.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FilmTests {

    @Mock
    private IFilmStarService filmStarService;
    @Mock
    private IActorService actorService;
    @Mock
    private IDirectorService directorService;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;


    @Test
    public void CreateFilm_Success(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of(2l));
        FilmStar filmStarActor = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        FilmStar filmStarDirector = FilmStar.builder()
                .id(2l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        Film film = Film.builder()
                .id(1l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();

        Actor actor = new Actor(new ActorId(1l,1l),film, filmStarActor);
        Director director = new Director(new DirectorId(2l,1l), film, filmStarDirector);

        film.setDirectors(List.of(director));
        film.setActors(List.of(actor));

        //Mock functions

        when(filmStarService.getFilmStarById(1l)).thenReturn(filmStarActor);
        when(filmStarService.getFilmStarById(2l)).thenReturn(filmStarDirector);

        when(actorService.saveActor(Mockito.any(Actor.class))).thenReturn(actor);

        when(directorService.saveDirector(Mockito.any(Director.class))).thenReturn(director);

        when(filmRepository.save(Mockito.any(Film.class))).thenReturn(film);


        FilmPageDTO dto = filmService.createFilm(request);

        assertThat(dto).isNotNull();
        assertThat(dto.actors()).isNotNull();
        assertThat(dto.actors()).isNotEmpty();
        assertThat(dto.directors()).isNotNull();
        assertThat(dto.directors()).isNotEmpty();

    }

    @Test
    public void FilmCreate_DuplicateTitle_410(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of(2l));
        Film film = Film.builder()
                .id(1l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();

        when(filmRepository.getFilmByTitle("title")).thenReturn(film);

        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(410,e.getCode());
            assertEquals("Film with this title already exists",e.getMessage());
        }

    }

    @Test
    public void FilmCreate_NoActors_412(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(),
                List.of(2l));

        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(412,e.getCode());
            assertEquals("Film has to have at least one actor",e.getMessage());
        }



    }
    @Test
    public void FilmCreate_NoDirectors_412(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of());
        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(412,e.getCode());
            assertEquals("Film has to have at least one director",e.getMessage());
        }
    }

    @Test
    public void FilmCreate_ActorNotPresent_409(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of(2l));



        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(409,e.getCode());
            assertEquals("Actor id=1 doesn't exist",e.getMessage());
        }
    }

    @Test
    public void FilmCreate_DirectorNotPresent_409(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of(2l));
        FilmStar filmStarActor = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();

        when(filmStarService.getFilmStarById(1l)).thenReturn(filmStarActor);

        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(409,e.getCode());
            assertEquals("Director id=2 doesn't exist",e.getMessage());
        }
    }

    @Test
    public void FilmCreate_ActorsNotUnique_411(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l,1l),
                List.of(2l));
        FilmStar filmStarActor = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        FilmStar filmStarDirector = FilmStar.builder()
                .id(2l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();

        when(filmStarService.getFilmStarById(1l)).thenReturn(filmStarActor);
        when(filmStarService.getFilmStarById(2l)).thenReturn(filmStarDirector);

        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(411,e.getCode());
            assertEquals("Actors are not unique",e.getMessage());
        }
    }
    @Test
    public void FilmCreate_DirectorsNotUnique_411(){
        CreateFilmRequest request = new CreateFilmRequest("title", "description",
                List.of(1l),
                List.of(2l,2l));
        FilmStar filmStarActor = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        FilmStar filmStarDirector = FilmStar.builder()
                .id(2l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();

        when(filmStarService.getFilmStarById(1l)).thenReturn(filmStarActor);
        when(filmStarService.getFilmStarById(2l)).thenReturn(filmStarDirector);

        try{
            filmService.createFilm(request);
            assert false;
        }catch (ApplicationException e){
            assertEquals(411,e.getCode());
            assertEquals("Directors are not unique",e.getMessage());
        }
    }

    @Test
    public void GetFilmPreview_Success(){
        Film film = Film.builder()
                .id(1l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();

        when(filmRepository.getFilmById(1l)).thenReturn(film);
        FilmPreviewDTO dto = filmService.getFilmPreview(1l);

        assertThat(dto).isNotNull();
        assertEquals(1l,dto.id());
        assertEquals("title",dto.title());
        assertEquals(0f,dto.rating());
    }

    @Test
    public void GetFilmPreview_FilmNotFound_404(){
        try{
            FilmPreviewDTO dto = filmService.getFilmPreview(1l);
            assert false;
        }catch (ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Film not found",e.getMessage());
        }
    }

    @Test
    public void GetFilmPage_Success(){
        FilmStar filmStarActor = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        FilmStar filmStarDirector = FilmStar.builder()
                .id(2l)
                .name("Joe")
                .lastname("Doe")
                .starredFilms(new ArrayList<>())
                .directedFilms(new ArrayList<>())
                .build();
        Film film = Film.builder()
                .id(1l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();

        Actor actor = new Actor(new ActorId(1l,1l),film, filmStarActor);
        Director director = new Director(new DirectorId(2l,1l), film, filmStarDirector);
        film.setDirectors(List.of(director));
        film.setActors(List.of(actor));

        when(filmRepository.getFilmById(1l)).thenReturn(film);

        FilmPageDTO dto = filmService.getFilmPage(1l);

        assertThat(dto).isNotNull();
        assertThat(dto.directors()).isNotNull();
        assertThat(dto.directors()).isNotEmpty();
        assertThat(dto.actors()).isNotNull();
        assertThat(dto.actors()).isNotEmpty();
        assertEquals(film.getTitle(),dto.title());
        assertEquals(film.getDescription(),dto.description());

    }

    @Test
    public void GetFilmPage_NotFound_404(){
        try{
            FilmPageDTO dto = filmService.getFilmPage(1l);
            assert  false;
        }catch(ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Film not found", e.getMessage());
        }
    }


    @Test
    public void GetAllFilms_Success(){
        Film film = Film.builder()
                .id(1l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();
        Film film2 = Film.builder()
                .id(2l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();
        Film film3 = Film.builder()
                .id(3l)
                .title("title")
                .description("description")
                .actors(new ArrayList<>())
                .directors(new ArrayList<>())
                .ratings(List.of())
                .build();

        List<Film> films = List.of(film,film2, film3);

        when(filmRepository.findAll()).thenReturn(films);

        FilmListDTO dto = filmService.getAllFilms();

        assertThat(dto).isNotNull();
        assertThat(dto.films()).isNotNull();
        assertThat(dto.films().size()).isEqualTo(3);
    }


}
