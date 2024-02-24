package com.example.filmview.Director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorService implements IDirectorService{
    private final DirectorRepository directorRepository;

    @Override
    public Director saveDirector(Director director){
        return directorRepository.save(director);
    }
}
