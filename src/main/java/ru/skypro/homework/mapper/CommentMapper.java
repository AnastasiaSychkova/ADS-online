package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;

import java.util.ArrayList;
import java.util.List;
@Component
public class CommentMapper {


    /** Метод для конвертации Comment в CommentDto */
    public CommentDto commentIntoCommentDto(Comment comment){
        Image image = comment.getAuthor().getImage();
        return new CommentDto(comment.getAuthor().getId(), image == null? null: ("/images/" + image.getId()), comment.getAuthor().getFirstName(), comment.getCreatedAt(), comment.getId(), comment.getText());
    }


    /** Метод для конвертации list Comment в CommentsDto */
    public CommentsDto listCommentIntoCommentsDto(List<Comment> comments){
        CommentsDto commentsDto = new CommentsDto();
        List<CommentDto> results = new ArrayList<>();

        for (Comment comment : comments){
            results.add(commentIntoCommentDto(comment));
        }

        commentsDto.setCount(comments.size());
        commentsDto.setResults(results);

        return commentsDto;
    }
}
