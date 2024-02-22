package com.example.filmview.Filmview.Mocks.Film;

import lombok.*;

import java.util.List;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockCreateFilmRequest {
    private String title;
    private String description;
    private List<Long> actors;
    private List<Long> directors;
}
