package com.example.filmview.Director;

import com.example.filmview.Film.Film;
import com.example.filmview.FilmStar.FilmStar;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Director {
    @Id
    private DirectorId Id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("filmId")
    private Film film;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("filmStarId")
    private FilmStar filmStar;
}
