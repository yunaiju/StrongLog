package com.toFinish.StrongLog.domain.comment.service;

import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.comment.repository.CommentRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("Comment Not Found");
        }
    }

    public void addComment(String content, User author, BasicArticle article) {
        Comment comment = new Comment(content, author, article, LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void editComment(Comment comment, String content) {
        comment.updateComment(content);
        this.commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public List<Comment> getCommentList(BasicArticle article) {
        return this.commentRepository.findAllByArticle(article);
    }

}
