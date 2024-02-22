package com.example.filmview.Director;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DirectorRepository extends JpaRepository<Director,DirectorId> {
}
