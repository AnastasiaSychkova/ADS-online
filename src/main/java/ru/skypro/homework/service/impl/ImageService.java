package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.util.InputMismatchException;

/** Сервис для работы с сущностью Image */
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    /** Метод для конвертации файла в сущность Image */
    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setContentType(file.getContentType());
        image.setBytes(file.getBytes());
        return image;
    }


    /** Метод добавления фотографии в бд */
    public Image updateImage(MultipartFile file) throws IOException {
        Image image;
        if (file == null || file.getSize() == 0) {
            throw new InputMismatchException();
        }

        image = toImageEntity(file);
        imageRepository.save(image);
        return image;
    }


    /** Метод для обновления картинки без сохранения в бд */
    public Image updateImageWithoutSaveInDb(MultipartFile file) throws IOException {
        Image image;
        if (file == null || file.getSize() == 0) {
            throw new InputMismatchException();
        }

        image = toImageEntity(file);
        return image;
    }


    /** Метод для сохранения */
    public void save(Image image){
        imageRepository.save(image);
    }


    /** Метод для получения массива byte */
    public byte[] getImage(Long id){
        Image image = imageRepository.findImageById(id);

        if (image != null) {
            return image.getBytes();
        } else {
            throw new NotFoundException("Image not found!");
        }
    }
}
