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

import java.util.List;

/** Сервис для работы с сущностью Comment */
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


    /** Метод для получения списка комментариев */
    public CommentsDto getComments(Long id) {
        List<Comment> comments = commentRepository.findAllByAdId(id);

        return commentMapper.listCommentIntoCommentsDto(comments);
    }


    /** Метод для создания и сохранения комментария */
    public CommentDto createComment(Long id, String login, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        Ad ad = adService.findAdById(id);
        if(ad == null){
            return null;
        }
        User author = userService.getUserByLogin(login);
        if (author == null) {
            return null;
        }

        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(createOrUpdateComment.getText());

        commentRepository.save(comment);
        return commentMapper.commentIntoCommentDto(comment);
    }


    /** Метод для удаления комментария */
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @commentService.authorNameByCommentId(#id)")
    public boolean deleteComment(Long id, Long adId) {

        if (!commentRepository.existsById(id)) {
            return false;
        }else {
            commentRepository.deleteById(id);
            return true;
        }
    }


    /** Метод для обновления комментария */
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @commentService.authorNameByCommentId(#id)")
    public CommentDto updateComment(Long id, CreateOrUpdateComment createOrUpdateComment, Long adId) {
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null) {
            return null;
        }

        comment.setText(createOrUpdateComment.getText());
        commentRepository.save(comment);
        return commentMapper.commentIntoCommentDto(comment);
    }


    /** Метод для получения из бд имени автора по id комментария */
    public String authorNameByCommentId(Long id){
        return commentRepository.findById(id).map(comment -> comment.getAuthor().getEmail()).orElseThrow(RuntimeException::new);
    }
}
