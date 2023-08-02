package ru.skypro.homework.dto.comment;

import lombok.Data;
import ru.skypro.homework.model.Image;

@Data
public class CommentDto {
    private Long authorId;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Long pk;
    private String text;

    public CommentDto(Long authorId, String authorImage, String authorFirstName, Long createdAt, Long pk, String text) {
        this.authorId = authorId;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }
}
