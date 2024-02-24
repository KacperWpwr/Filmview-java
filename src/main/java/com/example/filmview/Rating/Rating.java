package com.example.filmview.Rating;

import com.example.filmview.Film.Film;
import com.example.filmview.User.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    @SequenceGenerator(
            name = "rating_sequence",
            sequenceName = "rating_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "rating_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    private long Id;

    @Column
    private String description;

    @Column(nullable = false)
    private float rating;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private User rating_user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Film rated_film;
}
