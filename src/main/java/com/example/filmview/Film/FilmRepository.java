package com.example.filmview.Film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film,Long> {

    @Query("select film from Film  as film where film.title=:title")
    Film getFilmByTitle(String title);

    @Query("select film from Film  as film where film.id=:id")
    Film getFilmById(Long id);
}
