package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(userService.getUserByLogin(login))")
    public AdDto createAd(String login, MultipartFile image, CreateOrUpdateAdDto createOrUpdateAdDto) throws IOException {
        User user = userService.getUserByLogin(login);
        Ad ad = new Ad(user, createOrUpdateAdDto.getPrice(), createOrUpdateAdDto.getTitle(), createOrUpdateAdDto.getDescription());
        adRepository.save(ad);

        Image image1 = imageService.updateAdImage(image);

        ad.setImage(image1);
        adRepository.save(ad);

        return adMapper.adIntoAdDto(ad);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(adService.getAdAuthorName(id))")
    public FullAdDto getFullAd(Long id) {
        Ad ad = adRepository.findAdById(id);
        if (ad == null) {
            return null;
        }
        return adMapper.adIntoFullAdDto(ad);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(adService.getAdAuthorName(id))")
    public boolean deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            return false;
        }
        adRepository.deleteById(id);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(adService.getAdAuthorName(id))")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(userService.getUserByLogin(login))")
    public AdsDto getMyAds(String login) {
        User user = userService.getUserByLogin(login);
        return adMapper.adIntoAdsDto(user.getUserAds());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(adService.getAdAuthorName(id))")
    public Ad findAdById(Long id){
        return adRepository.findAdById(id);
    }

    public String getAdAuthorName(Long id){
        return adRepository.findById(id).map(ad -> ad.getAuthor().getEmail()).orElse(null);
    }

    public void updateAdImageInDb(MultipartFile file, Long id) throws IOException {
        Image image = imageService.updateAdImage(file);
        Ad ad = findAdById(id);

        ad.setImage(image);
        adRepository.save(ad);
    }
}
