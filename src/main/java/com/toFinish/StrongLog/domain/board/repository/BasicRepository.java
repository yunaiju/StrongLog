package com.toFinish.StrongLog.domain.board.repository;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicRepository extends JpaRepository<BasicArticle, Long> {
    List<BasicArticle> findAllByAuthor(User author);
    List<BasicArticle> findAllByAuthorAndPrivacyFalse(User author);
    List<BasicArticle> findAllByAuthorAndArticleType(User author, ArticleType articleType);
    List<BasicArticle> findAllByAuthorAndArticleTypeAndPrivacyFalse(User author, ArticleType articleType);
    List<BasicArticle> findTop20ByPrivacyFalseOrderByTimeDesc();
    List<BasicArticle> findTop10ByArticleTypeAndPrivacyFalseOrderByTimeDesc(ArticleType articleType);
}
