package com.toFinish.StrongLog.domain.board.service;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.ArticleLike;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.ArticleLikeRepository;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleLikeServiceTest {
    @Autowired private UserRepository userRepository;
    @Autowired private BasicRepository basicRepository;
    @Autowired private ArticleLikeRepository articleLikeRepository;
    @Autowired private ArticleLikeService articleLikeService;

    private User user1;
    private User user2;
    private BasicArticle article1;
    private BasicArticle article2;

    @BeforeEach
    public void beforeEach() {
        this.userRepository.deleteAll();
        this.basicRepository.deleteAll();
        this.articleLikeRepository.deleteAll();

        user1 = new User("user1","user1","user1");
        this.userRepository.save(user1);

        user2 = new User("user2","user2","user2");
        this.userRepository.save(user2);

        article1 = new BasicArticle(ArticleType.FEELINGS, "article1","글 내용"
                ,user1, LocalDateTime.now(),false);
        this.basicRepository.save(article1);

        article2 = new BasicArticle(ArticleType.FEELINGS, "article2","글 내용"
                ,user1, LocalDateTime.now(),false);
        this.basicRepository.save(article2);
    }

    @DisplayName("좋아요 여부 확인")
    @Test
    void isLike() {
        // given
        ArticleLike articleLike = new ArticleLike(user1, article1);
        this.articleLikeRepository.save(articleLike);

        // when
        boolean resultTrue = this.articleLikeService.isLike(user1, article1);
        boolean resultFalse = this.articleLikeService.isLike(user1, article2);

        // then
        assertThat(resultTrue).isTrue();
        assertThat(resultFalse).isFalse();
    }

    @DisplayName("좋아요 하기")
    @Test
    void likePost() {
        // given

        // when
        this.articleLikeService.likePost(user1, article1);
        boolean result = this.articleLikeService.isLike(user1, article1);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("좋아요 취소")
    @Test
    void unlikePost() {
        // given
        ArticleLike articleLike = new ArticleLike(user1, article1);
        this.articleLikeRepository.save(articleLike);

        // when
        this.articleLikeService.unlikePost(user1, article1);
        boolean result = this.articleLikeService.isLike(user1, article1);

        // then
        assertThat(result).isFalse();
    }
}