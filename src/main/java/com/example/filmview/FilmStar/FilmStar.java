package com.example.filmview.FilmStar;

import com.example.filmview.Actor.Actor;
import com.example.filmview.Director.Director;
import com.example.filmview.Image.Image;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class FilmStar {
    @SequenceGenerator(
            name = "filmstar_sequence",
            sequenceName = "filmstar_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "filmstar_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    private long id;

    @Column
    private String name;
    @Column
    private String lastname;

    @OneToOne(orphanRemoval = true)
    private Image image;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Actor> starredFilms;


    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Director> directedFilms;

    public void addDirectedFilm(Director director){
        directedFilms.add(director);
    }

    public void addStarredFilm(Actor actor){
        starredFilms.add(actor);
    }

}
