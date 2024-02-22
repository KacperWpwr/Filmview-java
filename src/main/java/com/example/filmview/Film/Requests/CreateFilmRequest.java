package com.example.filmview.Film.Requests;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateFilmRequest(@NotBlank String title,
                                @NotBlank String description,
                                @NotBlank List<Long> actors,
                                @NotBlank List<Long> directors) {
}
