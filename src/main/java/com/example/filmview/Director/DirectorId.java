package com.example.filmview.Director;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DirectorId implements Serializable {
    private Long filmStarId;
    private Long filmId;
}
