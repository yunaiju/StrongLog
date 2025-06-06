package com.toFinish.StrongLog.domain.board.service;

import com.toFinish.StrongLog.domain.board.entity.ArticleLike;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.ArticleLikeRepository;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final BasicRepository basicRepository;

    public boolean isLike(User user, BasicArticle article) {
        return this.articleLikeRepository.findByUserAndArticle(user, article).isPresent();
    }

    public void likePost(User user, BasicArticle article) {
        ArticleLike like = new ArticleLike(user, article);
        like.like();
        System.out.println("isliked : "+like.isLiked());
        this.articleLikeRepository.save(like);
        article.increaseLike();
        System.out.println("countLikes : "+article.getCountLikes());
        this.basicRepository.save(article);
    }

    public void unlikePost(User user, BasicArticle article) {
        Optional<ArticleLike> articleLike = this.articleLikeRepository.findByUserAndArticle(user, article);
        articleLike.ifPresent(like -> {
            this.articleLikeRepository.delete(like);
            article.decreaseLike();
            System.out.println("isliked : "+like.isLiked());
            System.out.println("countLikes : "+article.getCountLikes());
            this.basicRepository.save(article);
        });
    }
}
