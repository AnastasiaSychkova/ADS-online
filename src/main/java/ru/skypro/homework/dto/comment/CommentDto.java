package ru.skypro.homework.dto.comment;

import lombok.Data;

@Data
public class CommentDto {
    private Long author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Long pk;
    private String text;

    public CommentDto(Long author, String authorImage, String authorFirstName, Long createdAt, Long pk, String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }
}
