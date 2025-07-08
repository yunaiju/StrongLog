package com.toFinish.StrongLog.domain.board.service;
import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.global.exception.InvalidFormException;
import com.toFinish.StrongLog.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Page<BasicArticle> getArticlesByAuthor(int page, User author) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("time"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return this.basicRepository.findAllByAuthor(author, pageable);
    }

    public List<BasicArticle> getPublicArticleByAuthor(User author) { return this.basicRepository.findAllByAuthorAndPrivacyFalse(author); }
    public Page<BasicArticle> getPublicArticleByAuthor(int page, User author) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("time"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return this.basicRepository.findAllByAuthorAndPrivacyFalse(author, pageable);
    }

    public List<BasicArticle> getArticlesByAuthorAndArticleType(User author, ArticleType articleType) {
        return this.basicRepository.findAllByAuthorAndArticleType(author, articleType);
    }
    public Page<BasicArticle> getArticlesByAuthorAndArticleType(int page, User author, ArticleType articleType) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("time"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return this.basicRepository.findAllByAuthorAndArticleType(author, articleType, pageable);
    }

    public List<BasicArticle> getPublicArticlesByAuthorAndArticleType(User author, ArticleType articleType) {
        return this.basicRepository.findAllByAuthorAndArticleTypeAndPrivacyFalse(author, articleType);
    }
    public Page<BasicArticle> getPublicArticlesByAuthorAndArticleType(int page, User author, ArticleType articleType) {
        List<Sort.Order> sorts = new ArrayList();
        sorts.add(Sort.Order.desc("time"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return this.basicRepository.findAllByAuthorAndArticleTypeAndPrivacyFalse(author, articleType, pageable);
    }

    public Long addArticle(ArticleType articleType, String title, String content, User author, boolean privacy) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidFormException("제목은 필수입니다.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidFormException("내용은 필수입니다.");
        }

        BasicArticle article = new BasicArticle(articleType, title, content, author, LocalDateTime.now(), privacy);
        this.basicRepository.save(article);

        return article.getId();
    }

    public void editArticle(BasicArticle article, ArticleType articleType, String title, String content, boolean privacy) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidFormException("제목은 필수입니다.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidFormException("내용은 필수입니다.");
        }

        article.updateArticle(articleType, title, content, privacy);
        this.basicRepository.save(article);
    }

    public void deleteArticle(BasicArticle article) {
        if(article.getId()==null) {
            throw new DataNotFoundException("존재하지 않는 게시글");
        }

        BasicArticle normalArticle = basicRepository.findById(article.getId())
                .orElseThrow(()-> new DataNotFoundException("존재하지 않는 게시글"));
        
        this.basicRepository.delete(normalArticle);
    }

    public List<BasicArticle> getPopularArticles() {
        return this.basicRepository.findTop10ByPrivacyFalseOrderByCountLikesDescTimeDesc();
    }

    public List<BasicArticle> getPopularArticlesByType(ArticleType articleType) {
        return this.basicRepository.findTop10ByArticleTypeAndPrivacyFalseOrderByCountLikesDescTimeDesc(articleType);
    }

}
