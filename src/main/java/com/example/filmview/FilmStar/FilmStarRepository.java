package com.example.filmview.FilmStar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmStarRepository extends JpaRepository<FilmStar,Long> {


    @Query("select star from FilmStar  as star where star.id=:Id")
     FilmStar getFilmStarById(long Id);
}
