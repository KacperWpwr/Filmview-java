package com.example.filmview.Actor;

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
public class Actor {
    @EmbeddedId
    private ActorId Id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("filmId")
    private Film film;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("filmStarId")
    private FilmStar filmStar;

}
