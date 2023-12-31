package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ad.FullAdDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;

/**
 * Сервис для работы с сущностью Ad
 */
@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final AdMapper adMapper;

    public AdService(AdRepository adRepository, UserService userService, ImageService imageService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.adMapper = adMapper;
    }

    public AdsDto getAllAds() {
        return adMapper.adIntoAdsDto(adRepository.findAll());
    }


    /**
     * Метод для создания и сохранения объявления в бд
     */
    public AdDto createAd(String login, MultipartFile image, CreateOrUpdateAdDto createOrUpdateAdDto) throws IOException {
        User user = userService.getUserByLogin(login);
        Ad ad = new Ad(user, createOrUpdateAdDto.getPrice(), createOrUpdateAdDto.getTitle(), createOrUpdateAdDto.getDescription());

        Image image1 = imageService.updateImage(image);

        ad.setImage(image1);
        adRepository.save(ad);

        return adMapper.adIntoAdDto(ad);
    }


    /**
     * Метод для получения FullAdDto
     */
    public FullAdDto getFullAd(Long id) {
        Ad ad = adRepository.findAdById(id);
        if (ad == null) {
            return null;
        }
        return adMapper.adIntoFullAdDto(ad);
    }


    /**
     * Метод для удаления объявления
     */
    public boolean deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            return false;
        }
        adRepository.deleteById(id);
        return true;
    }


    /**
     * Метод для изменения объявления
     */
    public AdDto updateAd(Long id, CreateOrUpdateAdDto createOrUpdateAdDto) {
        Ad ad = adRepository.findAdById(id);

        if (ad == null) {
            return null;
        }

        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        ad.setDescription(createOrUpdateAdDto.getDescription());

        adRepository.save(ad);

        return adMapper.adIntoAdDto(ad);
    }


    /**
     * Метод для получения списка объявлений
     */
    public AdsDto getMyAds(String login) {
        User user = userService.getUserByLogin(login);
        return adMapper.adIntoAdsDto(user.getUserAds());
    }


    /**
     * Метод для получения объявления по id
     */
    public Ad findAdById(Long id) {
        return adRepository.findAdById(id);
    }

    public String getAdAuthorName(Long id) {
        return findAdById(id).getAuthor().getEmail();
    }


    /**
     * Метод для добавления/изменения картинки
     */
    public void updateAdImageInDb(MultipartFile file, Long id) {
        try {
            Ad ad = findAdById(id);

            Image image = imageService.updateImageWithoutSaveInDb(file);
            image.setId(ad.getId());
            imageService.save(image);

            ad.setImage(image);
            adRepository.save(ad);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
