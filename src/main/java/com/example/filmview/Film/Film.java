package com.example.filmview.Film;

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
public class Film {
    @SequenceGenerator(
            name = "film_sequence",
            sequenceName = "film_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "film_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "film",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Actor> actors;

    @OneToMany(mappedBy = "film",orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Director> directors;

    @OneToOne(orphanRemoval = true)
    private Image image;

    public Float getRating(){
        return 0.00f;
    }
}
