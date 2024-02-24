package com.example.filmview.Rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    @Query( "select case when count(r)>0 then true else false end from Rating as " +
            "r where r.rated_film.id=:film_id and r.rating_user.login=:username")
    public boolean hasRated(String username, Long film_id);


}
