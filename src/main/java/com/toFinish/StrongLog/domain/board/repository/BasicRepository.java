package com.toFinish.StrongLog.domain.board.repository;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicRepository extends JpaRepository<BasicArticle, Long> {
    List<BasicArticle> findAllByAuthor(User author);
    Page<BasicArticle> findAllByAuthor(Pageable pageable, User author);

    List<BasicArticle> findAllByAuthorAndPrivacyFalse(User author);
    Page<BasicArticle> findAllByAuthorAndPrivacyFalse(Pageable pageable, User author);

    List<BasicArticle> findAllByAuthorAndArticleType(User author, ArticleType articleType);
    Page<BasicArticle> findAllByAuthorAndArticleType(Pageable pageable, User author, ArticleType articleType);

    List<BasicArticle> findAllByAuthorAndArticleTypeAndPrivacyFalse(User author, ArticleType articleType);
    Page<BasicArticle> findAllByAuthorAndArticleTypeAndPrivacyFalse(Pageable pageable, User author, ArticleType articleType);

    List<BasicArticle> findTop10ByPrivacyFalseOrderByCountLikesDescTimeDesc();

    List<BasicArticle> findTop10ByArticleTypeAndPrivacyFalseOrderByCountLikesDescTimeDesc(ArticleType articleType);
}
