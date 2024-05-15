package com.example.filmview.FilmStar;


import com.example.filmview.FilmStar.DTO.FilmStarDTO;
import com.example.filmview.FilmStar.DTO.FilmStarListDTO;
import com.example.filmview.FilmStar.Requests.CreateFilmStarRequest;
import com.example.filmview.Image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/filmstar")
@RequiredArgsConstructor
public class FilmStarController {
    private final IFilmStarService filmStarService;
    private final IImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<FilmStarDTO> addFilmStar(@RequestBody CreateFilmStarRequest request){
        return new ResponseEntity<>(filmStarService.createFilmStar(request), HttpStatusCode.valueOf(201));
    }

    @PostMapping("/{id}/add/picture")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addPicture(@PathVariable Long id, @RequestParam("image") MultipartFile file){
        System.out.println(file.getName());
        imageService.createImage("film_star_"+id, file);

    }

    @GetMapping("/{id}/picture")
    public byte[] getFilmStarImage(@PathVariable Long id){
        return imageService.getImageFileById("film_star_"+id);
    }

    @GetMapping("/all")
    public ResponseEntity<FilmStarListDTO> getFilmStars(){
        return new ResponseEntity<>(filmStarService.getAllFilmStarsDTOs(),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}/display")
    public ResponseEntity<FilmStarDTO> getFilmStarDisplay(@PathVariable Long id){
        return new ResponseEntity<>(filmStarService.getFilmStarDisplay(id),HttpStatusCode.valueOf(200));
    }
}
