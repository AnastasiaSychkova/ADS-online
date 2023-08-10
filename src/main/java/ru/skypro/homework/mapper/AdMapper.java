package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.FullAdDto;
import ru.skypro.homework.model.Ad;

import java.util.ArrayList;
import java.util.List;
@Component
public class AdMapper {


    /** Метод для конвертации Ad в AdDto */
    public AdDto adIntoAdDto(Ad ad){
        return new AdDto(ad.getAuthor().getId(), "/images/" + ad.getImage().getId(), ad.getId(), ad.getPrice(), ad.getTitle());
    }


    /** Метод для конвертации Ad в AdsDto */
    public AdsDto adIntoAdsDto(List<Ad> ads){
        AdsDto adsDto = new AdsDto();
        List<AdDto> results = new ArrayList<>();

        for (Ad ad : ads){
            results.add(adIntoAdDto(ad));
        }

        adsDto.setCount(ads.size());
        adsDto.setResults(results);

        return adsDto;
    }


    /** Метод для конвертации Ad в FullAdDto */
    public FullAdDto adIntoFullAdDto(Ad ad){
        return new FullAdDto(ad.getId(), ad.getAuthor().getFirstName(), ad.getAuthor().getLastName(), ad.getDescription(), ad.getAuthor().getEmail(), "/images/" + ad.getImage().getId(), ad.getAuthor().getPhone(), ad.getPrice(), ad.getTitle());
    }
}
