package com.toFinish.StrongLog.domain.comment.service;

import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.comment.repository.CommentRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.global.exception.InvalidFormException;
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

    public Comment addComment(String content, User author, BasicArticle article) {
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidFormException("내용은 필수입니다.");
        }

        Comment comment = new Comment(content, author, article, LocalDateTime.now());
        return this.commentRepository.save(comment);
    }

    public void editComment(Comment comment, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidFormException("내용은 필수입니다.");
        }

        comment.updateComment(content);
        this.commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        if(comment.getId()==null) {
            throw new DataNotFoundException("존재하지 않는 댓글");
        }
        
        this.commentRepository.delete(comment);
    }

    public List<Comment> getCommentList(BasicArticle article) {
        return this.commentRepository.findAllByArticle(article);
    }

}
