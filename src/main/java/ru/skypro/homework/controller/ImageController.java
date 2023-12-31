package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.impl.ImageService;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/images/{id}", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public byte[] getImage(@PathVariable Long id) {
        return imageService.getImage(id);
    }
}
