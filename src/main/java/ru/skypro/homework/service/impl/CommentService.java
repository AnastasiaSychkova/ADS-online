package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(commentService.authorNameByCommentId(id))")
    public CommentsDto getComments(Long id) {
        List<CommentsDto> commentsDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByAdId(id);

        return commentMapper.listCommentIntoCommentsDto(comments);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(commentService.authorNameByCommentId(id))")
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(commentService.authorNameByCommentId(id))")
    public boolean deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            return false;
        }
        commentRepository.deleteById(id);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name.equals(commentService.authorNameByCommentId(id))")
    public CommentDto updateComment(Long id, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null) {
            return null;
        }

        comment.setText(createOrUpdateComment.getText());
        return commentMapper.commentIntoCommentDto(comment);
    }

    public String authorNameByCommentId(Long id){
        return commentRepository.findById(id).map(comment -> comment.getAuthor().getEmail()).orElse(null);
    }
}
