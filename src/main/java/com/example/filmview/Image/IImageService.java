package com.example.filmview.Image;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    Image saveImage(Image image);
    void createImage(String image_id, MultipartFile file);
    Image getImageById(String id);
    byte[] getImageFileById(String id);

}
