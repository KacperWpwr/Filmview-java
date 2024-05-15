package com.example.filmview.Filmview.ServiceTests;


import com.example.filmview.Application.ApplicationException;
import com.example.filmview.FilmStar.DTO.FilmStarDTO;
import com.example.filmview.FilmStar.DTO.FilmStarListDTO;
import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarRepository;
import com.example.filmview.FilmStar.FilmStarService;
import com.example.filmview.FilmStar.Requests.CreateFilmStarRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FilmStarTests {
    @Mock
    private FilmStarRepository filmStarRepository;

    @InjectMocks
    private FilmStarService filmStarService;


    @Test
    public void CreateFilmStar_Success(){
        CreateFilmStarRequest request = new CreateFilmStarRequest("Joe", "Doe");
        FilmStar filmStar = FilmStar.builder()
                .name(request.name())
                .lastname(request.lastname())
                .build();

        when(filmStarRepository.save(Mockito.any(FilmStar.class))).thenReturn(filmStar);

        FilmStarDTO dto = filmStarService.createFilmStar(request);

        assertThat(dto).isNotNull();
        assertEquals(request.name(), dto.name());
        assertEquals(request.lastname(), dto.lastname());
    }

    @Test
    public void GetFilmStarDisplay_Success(){
        FilmStar filmStar = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .build();

        when(filmStarRepository.getFilmStarById(1l)).thenReturn(filmStar);

        FilmStarDTO dto = filmStarService.getFilmStarDisplay(1l);

        assertThat(dto).isNotNull();
        assertEquals(filmStar.getName(), dto.name());
        assertEquals(filmStar.getLastname(), dto.lastname());
        assertEquals(filmStar.getId(), dto.id());

    }

    @Test
    public void GetFilmStarDisplay_NotFound_404(){
        when(filmStarRepository.getFilmStarById(1l)).thenReturn(null);

        try{
            FilmStarDTO dto = filmStarService.getFilmStarDisplay(1l);
            assert false;
        }catch (ApplicationException e){
            assertEquals(404,e.getCode());
            assertEquals("Film Star not found", e.getMessage());
        }
    }

    @Test
    public void GetAllFilmStarsDTOs_Success(){
        FilmStar filmStar = FilmStar.builder()
                .id(1l)
                .name("Joe")
                .lastname("Doe")
                .build();
        FilmStar filmStar2 = FilmStar.builder()
                .id(2l)
                .name("Joe")
                .lastname("Doe")
                .build();
        FilmStar filmStar3 = FilmStar.builder()
                .id(3l)
                .name("Joe")
                .lastname("Doe")
                .build();

        List<FilmStar> stars = List.of(filmStar, filmStar2, filmStar3);

        when(filmStarRepository.findAll()).thenReturn(stars);

        FilmStarListDTO dto = filmStarService.getAllFilmStarsDTOs();
        assertThat(dto).isNotNull();
        assertThat(dto.stars()).isNotNull();
        assertEquals(3,dto.stars().size());
    }
}
