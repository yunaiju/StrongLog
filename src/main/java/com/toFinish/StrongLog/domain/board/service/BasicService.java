package com.toFinish.StrongLog.domain.board.service;
import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BasicService {
    private final BasicRepository basicRepository;

    public BasicArticle getArticle(Long id) {
        Optional<BasicArticle> article = this.basicRepository.findById(id);
        if(article.isPresent()) {
            return article.get();
        } else {
            throw new DataNotFoundException("Article Not Found");
        }
    }

    public List<BasicArticle> getArticlesByAuthor(User author) {
        return this.basicRepository.findAllByAuthor(author);
    }

    public List<BasicArticle> getPublicArticleByAuthor(User author) { return this.basicRepository.findAllByAuthorAndPrivacyFalse(author); }

    public List<BasicArticle> getArticlesByAuthorAndArticleType(User author, ArticleType articleType) {
        return this.basicRepository.findAllByAuthorAndArticleType(author, articleType);
    }

    public List<BasicArticle> getPublicArticlesByAuthorAndArticleType(User author, ArticleType articleType) {
        return this.basicRepository.findAllByAuthorAndArticleTypeAndPrivacyFalse(author, articleType);
    }

    public Long addArticle(ArticleType articleType, String title, String content, User author, boolean privacy) {
        BasicArticle article = new BasicArticle(articleType, title, content, author, LocalDateTime.now(), privacy);
        this.basicRepository.save(article);

        return article.getId();
    }

    public void editArticle(BasicArticle article, ArticleType articleType, String title, String content, boolean privacy) {
        article.updateArticle(articleType, title, content, privacy);
        this.basicRepository.save(article);
    }

    public void deleteArticle(BasicArticle article) {
        this.basicRepository.delete(article);
    }

    public boolean IsMyArticle() {
        return true;
    }

    public List<BasicArticle> getPopularArticles() {
        return this.basicRepository.findTop20ByPrivacyFalseOrderByTimeDesc();
    }

    public List<BasicArticle> getPopularArticlesByType(ArticleType articleType) {
        return this.basicRepository.findTop10ByArticleTypeAndPrivacyFalseOrderByTimeDesc(articleType);
    }

}
