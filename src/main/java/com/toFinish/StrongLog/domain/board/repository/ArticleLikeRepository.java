package com.toFinish.StrongLog.domain.board.repository;

import com.toFinish.StrongLog.domain.board.entity.ArticleLike;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByUserAndArticle(User user, BasicArticle article);
}
