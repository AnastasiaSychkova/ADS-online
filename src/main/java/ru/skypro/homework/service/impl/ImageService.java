package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserService userService;
    public ImageService(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public void updateUserAvatar(MultipartFile file, String login) throws IOException {
        Image image = null;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            imageRepository.save(image);
        }

        User user = userService.getUserByLogin(login);

        user.setImage(image);
        userService.updateUserImageDb(user);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setContentType(file.getContentType());
        image.setBytes(file.getBytes());
        return image;
    }

    public Image updateAdImage(MultipartFile file) throws IOException {
        Image image = null;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            imageRepository.save(image);
        }
        return image;
    }

    public byte[] getImage(Long id){
        return imageRepository.findById(id).get().getBytes();
    }
}
