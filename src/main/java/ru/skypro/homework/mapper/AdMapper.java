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

    public AdDto adIntoAdDto(Ad ad){
        return new AdDto(ad.getAuthor().getId(), ad.getImage(), ad.getId(), ad.getPrice(), ad.getTitle());
    }

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

    public FullAdDto adIntoFullAdDto(Ad ad){
        return new FullAdDto(ad.getId(), ad.getAuthor().getFirstName(), ad.getAuthor().getLastName(), ad.getDescription(), ad.getAuthor().getEmail(), ad.getImage(), ad.getAuthor().getPhone(), ad.getPrice(), ad.getTitle());
    }
}
