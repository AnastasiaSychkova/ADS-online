package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.util.InputMismatchException;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

  /*  public void updateUserAvatar(MultipartFile file, String login) throws IOException {
        Image image;
        if (file == null || file.getSize() == 0) {
            throw new InputMismatchException();
        }

        image = toImageEntity(file);
        imageRepository.save(image);

        User user = userService.getUserByLogin(login);

        user.setImage(image);
        userService.updateUserImageDb(user);
    }*/

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setContentType(file.getContentType());
        image.setBytes(file.getBytes());
        return image;
    }

    public Image updateImage(MultipartFile file) throws IOException {
        Image image;
        if (file == null || file.getSize() == 0) {
            throw new InputMismatchException();
        }

        image = toImageEntity(file);
        imageRepository.save(image);
        return image;
    }

    public byte[] getImage(Long id){
        return imageRepository.findImageById(id).getBytes();
    }
}
