package com.example.filmview.Film;

import com.example.filmview.Actor.Actor;
import com.example.filmview.Director.Director;
import com.example.filmview.Image.Image;
import com.example.filmview.Rating.Rating;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.ArrayList;
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
    private long id;

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

    @OneToMany(orphanRemoval = true, mappedBy = "rated_film", fetch = FetchType.EAGER)
    private List<Rating> ratings;




    public Float getRating(){
        float sum= 0.00f;
        if (ratings == null ) return 0f;
        if (ratings.isEmpty()) return 0f;

        for(Rating r : ratings){
            sum+=r.getRating();
        }
        return (float)Math.floor(sum/ratings.size()*100)/100;
    }
}
