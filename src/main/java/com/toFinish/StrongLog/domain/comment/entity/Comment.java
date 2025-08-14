package com.toFinish.StrongLog.domain.comment.entity;

import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity @Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private User author;

    @ManyToOne
    private BasicArticle article;

    private LocalDateTime time;

    public Comment(String content, User author, BasicArticle article, LocalDateTime time) {
        this.content = content;
        this.author = author;
        this.article = article;
        this.time = time;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
