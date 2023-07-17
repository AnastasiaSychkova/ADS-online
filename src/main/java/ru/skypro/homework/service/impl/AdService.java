package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ad.FullAdDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper adMapper;

    public AdService(AdRepository adRepository, UserService userService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.adMapper = adMapper;
    }

    public AdsDto getAllAds() {
        return adMapper.adIntoAdsDto(adRepository.findAll());
    }


    public AdDto createAd(String login, MultipartFile image, CreateOrUpdateAdDto createOrUpdateAdDto) {
        User user = userService.getUserByLogin(login);
        Ad ad = new Ad(user, createOrUpdateAdDto.getPrice(), createOrUpdateAdDto.getTitle(), createOrUpdateAdDto.getDescription());

        adRepository.save(ad);
        return adMapper.adIntoAdDto(ad);
    }

    public FullAdDto getFullAd(Long id) {
        Ad ad = adRepository.findAdById(id);
        if (ad == null) {
            return null;
        }
        return adMapper.adIntoFullAdDto(ad);
    }

    public boolean deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            return false;
        }
        adRepository.deleteById(id);
        return true;
    }

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

    public AdsDto getMyAds(String login) {
        User user = userService.getUserByLogin(login);
        return adMapper.adIntoAdsDto(user.getUserAds());
    }

    public Ad findAdById(Long id){
        return adRepository.findAdById(id);
    }

    //public void updateImage(Long id, MultipartFile image) {
    //}
}
