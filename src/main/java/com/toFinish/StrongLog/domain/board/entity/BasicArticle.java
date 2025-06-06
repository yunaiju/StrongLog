package com.toFinish.StrongLog.domain.board.entity;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

import java.io.PipedReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class BasicArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArticleType articleType;

    private String title;

    private String content;

    @ManyToOne
    private User author;

    private LocalDateTime time;

    private boolean privacy;

    private int countLikes = 0;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    public BasicArticle(ArticleType articleType, String title, String content, User author, LocalDateTime time, boolean privacy) {
        this.articleType = articleType;
        this.title = title;
        this.content = content;
        this.author = author;
        this.time = time;
        this.privacy = privacy;
    }

    public void updateArticle(ArticleType articleType, String title, String content, boolean privacy) {
        this.articleType = articleType;
        this.title = title;
        this.content = content;
        this.privacy = privacy;
    }

    public void increaseLike() {
        this.countLikes++;
    }

    public void decreaseLike() {
        this.countLikes--;
    }
}
