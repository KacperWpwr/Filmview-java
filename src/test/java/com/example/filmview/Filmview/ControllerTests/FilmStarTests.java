package com.example.filmview.Filmview.ControllerTests;

import com.example.filmview.FilmStar.FilmStar;
import com.example.filmview.FilmStar.FilmStarController;
import com.example.filmview.FilmStar.FilmStarService;
import com.example.filmview.FilmStar.Requests.CreateFilmStarRequest;
import com.example.filmview.Image.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = FilmStarController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FilmStarTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmStarService filmStarService;

    @MockBean
    private ImageService imageService;

    private FilmStar testStar;
    private CreateFilmStarRequest filmStarRequest;

    @BeforeEach
    public void init(){
        testStar = FilmStar.builder()
                .id(1l)
                .name("Test")
                .lastname("Star")
                .build();


        filmStarRequest = new CreateFilmStarRequest("Test", "Star");
    }



}
