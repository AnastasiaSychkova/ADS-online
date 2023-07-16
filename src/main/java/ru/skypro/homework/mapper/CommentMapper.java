package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentDto commentIntoCommentDto(Comment comment){
        return new CommentDto(comment.getAuthor().getId(), null, comment.getAuthor().getFirstName(), comment.getCreatedAt(), comment.getId(), comment.getText());
    }

    public static CommentsDto listCommentIntoCommentsDto(List<Comment> comments){
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
