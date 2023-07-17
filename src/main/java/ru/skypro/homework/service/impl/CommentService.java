package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdService adService;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, UserService userService, AdService adService, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.adService = adService;
        this.commentMapper = commentMapper;
    }

    public CommentsDto getComments(Long id) {
        List<CommentsDto> commentsDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByAdId(id);

        return commentMapper.listCommentIntoCommentsDto(comments);
    }

    public CommentDto createComment(Long id, String login, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        Ad ad = adService.findAdById(id);
        User author = userService.getUserByLogin(login);
        if (ad == null || author == null) {
            return null;
        }

        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(createOrUpdateComment.getText());

        commentRepository.save(comment);
        return commentMapper.commentIntoCommentDto(comment);
    }

    public boolean deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            return false;
        }
        commentRepository.deleteById(commentId);
        return true;
    }

    public CommentDto updateComment(Long commentId, CreateOrUpdateComment createOrUpdateComment){
        Comment comment = commentRepository.findCommentById(commentId);
        if(comment == null){
            return null;
        }

        comment.setText(createOrUpdateComment.getText());
        return commentMapper.commentIntoCommentDto(comment);
    }
}
