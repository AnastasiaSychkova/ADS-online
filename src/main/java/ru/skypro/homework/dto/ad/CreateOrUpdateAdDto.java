package ru.skypro.homework.dto.ad;

import lombok.Data;

@Data
public class CreateOrUpdateAdDto {
    private String title;
    private int price;
    private String description;

    public CreateOrUpdateAdDto(String title, int price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }
}
