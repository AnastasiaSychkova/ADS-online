package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ad.FullAdDto;
import ru.skypro.homework.service.impl.AdService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAds(@RequestPart("properties") CreateOrUpdateAdDto createOrUpdateAdDto,
                                        @RequestPart("image") MultipartFile image) throws IOException {

        AdDto adDto = adService.createAd(SecurityContextHolder.getContext().getAuthentication().getName(), image, createOrUpdateAdDto);
        return  new ResponseEntity<>(adDto,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAdDto> getFullAd(@PathVariable("id") Long id) {
        FullAdDto fullAdDto = adService.getFullAd(id);

        if(fullAdDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(fullAdDto);
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable("id") Long id) {
        if(adService.deleteAd(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable("id") Long id, @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto) {
        AdDto adDto = adService.updateAd(id,createOrUpdateAdDto);
        if(adDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(adDto);
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDto> getMyAds() {
        AdsDto adsDto = adService.getMyAds(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(adsDto);
    }

    @PreAuthorize("hasAuthority('ADMIN') OR authentication.name == @adService.getAdAuthorName(#id)")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id,
                                         @RequestPart MultipartFile image) {
        adService.updateAdImageInDb(image, id);
        return ResponseEntity.ok().build();
    }
}
