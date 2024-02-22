package com.example.filmview.Filmview.Mocks.Film;


import com.example.filmview.Filmview.Mocks.FilmStar.MockFilmStarDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockFilmPageDTO {
    private Long id;
    private String title;
    private String description;
    private Float rating;
    private List<MockFilmStarDTO> actors;
    private List<MockFilmStarDTO> directors;
}
