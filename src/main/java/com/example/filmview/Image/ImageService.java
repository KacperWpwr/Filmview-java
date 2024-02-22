package com.example.filmview.Image;

import com.example.filmview.Application.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;
    public Image saveImage(Image image){
        return imageRepository.save(image);
    }
    public void createImage(String image_id, MultipartFile file){
        try{
            Image image = new Image(image_id,file.getBytes(), file.getName());
            imageRepository.save(image);
        }catch (IOException e){
            throw new ApplicationException(e.getMessage(), 500);
        }
    }


    public Image getImageById(String id){
        return imageRepository.getReferenceById(id);
    }
    @Transactional
    public byte[] getImageFileById(String id){
        Optional<Image> image = imageRepository.findById(id);

        if(image.isEmpty()){
            try{
//                File resource = new ClassPathResource(
//                        "data/employees.dat").getFile();
                return resourceLoader.getResource("classpath:images/no_image.png").getContentAsByteArray();
            }catch( IOException e){
                throw new ApplicationException("Unknown Error",500);
            }
        }

        return image.get().getImage();
    }
}
