package com.example.filmview.Actor;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Getter@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ActorId implements Serializable {
    private Long filmStarId;
    private Long filmId;
}
