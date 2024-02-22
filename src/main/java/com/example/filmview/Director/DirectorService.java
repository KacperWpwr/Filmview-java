package com.example.filmview.Director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;


    public Director saveDirector(Director director){
        return directorRepository.save(director);
    }
}
