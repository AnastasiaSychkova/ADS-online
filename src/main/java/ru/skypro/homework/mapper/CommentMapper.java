package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.model.Comment;

import java.util.ArrayList;
import java.util.List;
@Component
public class CommentMapper {

    public CommentDto commentIntoCommentDto(Comment comment){
        return new CommentDto(comment.getAuthor().getId(), comment.getAuthor().getImage(), comment.getAuthor().getFirstName(), comment.getCreatedAt(), comment.getId(), comment.getText());
    }

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
