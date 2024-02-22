package com.example.filmview.Image;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Image {

    @Id
    private String id;

    @Column
    @Lob
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] image;

    @Column
    private String name;
}
