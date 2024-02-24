package com.example.filmview.Actor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActorService implements IActorService{
    private final ActorRepository actorRepository;

    public Actor saveActor(Actor actor){
        return actorRepository.save(actor);
    }

}
