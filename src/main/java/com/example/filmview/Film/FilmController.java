package com.example.filmview.Film;

import com.example.filmview.Film.DTO.FilmListDTO;
import com.example.filmview.Film.DTO.FilmPageDTO;
import com.example.filmview.Film.DTO.FilmPreviewDTO;
import com.example.filmview.Film.Requests.CreateFilmRequest;
import com.example.filmview.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/film")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<FilmPageDTO> createFilm(@RequestBody CreateFilmRequest request){
        return new ResponseEntity<>(filmService.createFilm(request), HttpStatusCode.valueOf(201));
    }
    @PostMapping("/{id}/add/picture")
    public void addPicture(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        imageService.createImage("film_"+id,file);
    }

    @GetMapping("/{id}/display")
    public ResponseEntity<FilmPageDTO> getFilmPage(@PathVariable Long id){
        return new ResponseEntity<>(filmService.getFilmPage(id),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<FilmPreviewDTO> getFilmPreview(@PathVariable Long id){
        return new ResponseEntity<>(filmService.getFilmPreview(id),HttpStatusCode.valueOf(200));
    }


    @GetMapping("/top/{top}")
    public ResponseEntity<FilmListDTO> getTopFilms(@PathVariable int top){
        return new ResponseEntity<>(filmService.getTopFilms(top),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/all")
    public ResponseEntity<FilmListDTO> getAllFilms(){
        System.out.println("Recieved");
        return new ResponseEntity<>(filmService.getAllFilms(),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}/picture")
    public byte[] getFilmImage(@PathVariable Long id){
        return imageService.getImageFileById("film_"+id);
    }

}
