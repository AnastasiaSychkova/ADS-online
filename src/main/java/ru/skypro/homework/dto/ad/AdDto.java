package ru.skypro.homework.dto.ad;

import lombok.Data;

@Data
public class AdDto {
    private Long author;
    private String image;
    private Long pk;
    private int price;
    private String title;

    public AdDto(Long author, String image, Long pk, int price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }
}
