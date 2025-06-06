package com.toFinish.StrongLog.domain.board.entity;

import com.toFinish.StrongLog.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class ArticleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private BasicArticle article;

    boolean isLiked = false;

    public ArticleLike(User user, BasicArticle article) {
        this.user = user;
        this.article = article;
    }

    public void like() {
        this.isLiked = true;
    }

    public void unlike() {
        this.isLiked = false;
    }
}
