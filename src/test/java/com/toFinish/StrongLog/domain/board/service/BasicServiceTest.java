package com.toFinish.StrongLog.domain.board.service;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.dto.BasicForm;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.global.exception.InvalidFormException;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BasicServiceTest {
    @Autowired private BasicService basicService;
    @Autowired private BasicRepository basicRepository;
    @Autowired private UserRepository userRepository;

    private User user;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        basicRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("test","test1234!","테스트씨");
        user2 = new User("test2","test1234!","테스트씨2");
        userRepository.save(user);
        userRepository.save(user2);
    }

    @DisplayName("게시글 가져오기 성공")
    @Test
    void getArticle_success() {
        //given
        BasicArticle article = new BasicArticle(ArticleType.FEELINGS, "감정일기 테스트","글 내용"
                ,user, LocalDateTime.now(),false);
        basicRepository.save(article);

        //when
        BasicArticle getArticle = basicService.getArticle(article.getId());

        //then
        assertThat(getArticle.getArticleType()).isEqualTo(ArticleType.FEELINGS);
        assertThat(getArticle.getTitle()).isEqualTo("감정일기 테스트");
        assertThat(getArticle.getContent()).isEqualTo("글 내용");
        assertThat(getArticle.getAuthor()).isEqualTo(user);
        assertThat(getArticle.isPrivacy()).isFalse();
    }

    @DisplayName("게시글 가져오기 실패")
    @Test
    void getArticle_fail() {
        //given
        Long notExistId = -1L;

        //when & then
        assertThrows(DataNotFoundException.class, ()->{
            basicService.getArticle(notExistId);
        });
    }

    @DisplayName("글쓴이로 게시글 목록 가져오기 성공")
    @Test
    void getArticlesByAuthor_success() {
        //given
        BasicArticle article1 = new BasicArticle(
                ArticleType.FEELINGS, "테스트1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(article1);

        BasicArticle article2 = new BasicArticle(
                ArticleType.FEELINGS, "테스트2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(article2);

        //when
        Page<BasicArticle> getArticles = basicService.getArticlesByAuthor(0,user);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).hasSize(1);
        assertThat(getArticles.getContent().get(0).getAuthor()).isEqualTo(user);
    }

    @DisplayName("글쓴이로 게시글 목록 가져오기 실패")
    @Test
    void getArticlesByAuthor_fail() {
        //given
        BasicArticle article1 = new BasicArticle(
                ArticleType.FEELINGS, "테스트1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(article1);

        //when
        Page<BasicArticle> getArticles = basicService.getArticlesByAuthor(0,user2);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).isEmpty();
    }

    @DisplayName("글쓴이로 공개 게시글 목록 가져오기 성공")
    @Test
    void getPublicArticleByAuthor_success() {
        //given
        BasicArticle publicArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "공개글1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(publicArticle1);

        BasicArticle privateArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "비밀글1", "글 내용",
                user, LocalDateTime.now(), true);
        basicRepository.save(privateArticle1);

        BasicArticle publicArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "공개글2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(publicArticle2);

        //when
        Page<BasicArticle> getArticles = basicService.getPublicArticleByAuthor(0,user);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).hasSize(1);
        assertThat(getArticles.getContent().get(0).getAuthor()).isEqualTo(user);
    }

    @DisplayName("글쓴이로 공개 게시글 목록 가져오기 실패")
    @Test
    void getPublicArticleByAuthor_fail() {
        // given
        BasicArticle privateArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "비밀글1", "글 내용",
                user, LocalDateTime.now(), true);
        basicRepository.save(privateArticle1);

        BasicArticle publicArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "공개글2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(publicArticle2);

        // when
        Page<BasicArticle> getArticles = basicService.getPublicArticleByAuthor(0,user);

        // then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).isEmpty();
    }

    @DisplayName("글쓴이랑 카테고리로 게시글 가져오기 성공")
    @Test
    void getArticlesByAuthorAndArticleType_success() {
        //given
        BasicArticle feelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "감정일기1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(feelingsArticle1);

        BasicArticle transcriptionArticle1 = new BasicArticle(
                ArticleType.TRANSCRIPTION, "필사1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(transcriptionArticle1);

        BasicArticle feelingsArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "감정일기2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(feelingsArticle2);

        //when
        Page<BasicArticle> getArticles = basicService.getArticlesByAuthorAndArticleType(0,user,ArticleType.FEELINGS);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).hasSize(1);
        assertThat(getArticles.getContent().get(0).getAuthor()).isEqualTo(user);
        assertThat(getArticles.getContent().get(0).getArticleType()).isEqualTo(ArticleType.FEELINGS);
    }

    @DisplayName("글쓴이랑 카테고리로 게시글 가져오기 실패")
    @Test
    void getArticlesByAuthorAndArticleType_fail() {
        //given
        BasicArticle feelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "감정일기1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(feelingsArticle1);

        BasicArticle transcriptionArticle1 = new BasicArticle(
                ArticleType.TRANSCRIPTION, "필사1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(transcriptionArticle1);

        BasicArticle feelingsArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "감정일기2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(feelingsArticle2);

        //when
        Page<BasicArticle> getArticles = basicService.getArticlesByAuthorAndArticleType(0,user2,ArticleType.TRANSCRIPTION);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).isEmpty();
    }

    @DisplayName("글쓴이랑 카테고리로 공개 게시글 가져오기 성공")
    @Test
    void getPublicArticlesByAuthorAndArticleType_success() {
        //given
        BasicArticle publicFeelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "공개_감정일기1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(publicFeelingsArticle1);

        BasicArticle privateFeelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "비공개_감정일기1","글 내용",
                user, LocalDateTime.now(), true);
        basicRepository.save(privateFeelingsArticle1);

        BasicArticle publicTranscriptionArticle1 = new BasicArticle(
                ArticleType.TRANSCRIPTION, "공개_필사1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(publicTranscriptionArticle1);

        BasicArticle publicFeelingsArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "공개_감정일기2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(publicFeelingsArticle2);

        //when
        Page<BasicArticle> getArticles = basicService.getPublicArticlesByAuthorAndArticleType(0,user,ArticleType.FEELINGS);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).hasSize(1);
        assertThat(getArticles.getContent().get(0).getAuthor()).isEqualTo(user);
        assertThat(getArticles.getContent().get(0).getArticleType()).isEqualTo(ArticleType.FEELINGS);
        assertThat(getArticles.getContent().get(0).isPrivacy()).isFalse();
    }

    @DisplayName("글쓴이랑 카테고리로 공개 게시글 가져오기 실패")
    @Test
    void getPublicArticlesByAuthorAndArticleType_fail() {
        //given
        BasicArticle publicFeelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "공개_감정일기1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(publicFeelingsArticle1);

        BasicArticle privateFeelingsArticle1 = new BasicArticle(
                ArticleType.FEELINGS, "비공개_감정일기1","글 내용",
                user, LocalDateTime.now(), true);
        basicRepository.save(privateFeelingsArticle1);

        BasicArticle publicTranscriptionArticle1 = new BasicArticle(
                ArticleType.TRANSCRIPTION, "공개_필사1", "글 내용",
                user, LocalDateTime.now(), false);
        basicRepository.save(publicTranscriptionArticle1);

        BasicArticle publicFeelingsArticle2 = new BasicArticle(
                ArticleType.FEELINGS, "공개_감정일기2", "글 내용",
                user2, LocalDateTime.now(), false);
        basicRepository.save(publicFeelingsArticle2);

        BasicArticle publicTranscriptionArticle2 = new BasicArticle(
                ArticleType.TRANSCRIPTION, "비공개_필사2", "글 내용",
                user2, LocalDateTime.now(), true);
        basicRepository.save(publicTranscriptionArticle2);

        //when
        Page<BasicArticle> getArticles = basicService.getPublicArticlesByAuthorAndArticleType(0,user2,ArticleType.TRANSCRIPTION);

        //then
        assertThat(getArticles).isNotNull();
        assertThat(getArticles.getContent()).isEmpty();
    }

    @DisplayName("게시글 등록 성공")
    @Test
    void addArticle_success() {
        //given
        BasicForm basicForm = new BasicForm();
        basicForm.setArticleType(ArticleType.FEELINGS);
        basicForm.setTitle("테스트");
        basicForm.setContent("글 내용");
        basicForm.setPrivacy(false);

        //when
        Long articleId = basicService.addArticle(basicForm.getArticleType(), basicForm.getTitle(), basicForm.getContent()
                ,user,basicForm.isPrivacy());
        BasicArticle getArticle = basicService.getArticle(articleId);

        //then
        assertThat(getArticle).isNotNull();
        assertThat(getArticle.getId()).isEqualTo(articleId);
        assertThat(getArticle.getArticleType()).isEqualTo(basicForm.getArticleType());
        assertThat(getArticle.getTitle()).isEqualTo(basicForm.getTitle());
        assertThat(getArticle.getContent()).isEqualTo(basicForm.getContent());
        assertThat(getArticle.isPrivacy()).isEqualTo(basicForm.isPrivacy());
    }

    @DisplayName("게시글 등록 실패 - 제목 공백")
    @Test
    void addArticle_fail_when_title_isEmpty() {
        //given
        BasicForm basicForm = new BasicForm();
        basicForm.setArticleType(ArticleType.FEELINGS);
        basicForm.setTitle("");
        basicForm.setContent("글 내용");
        basicForm.setPrivacy(false);

        //when & then
        assertThrows(InvalidFormException.class, ()-> {
            basicService.addArticle(basicForm.getArticleType(), basicForm.getTitle(), basicForm.getContent(),
                    user, basicForm.isPrivacy());
        });
    }

    @DisplayName("게시글 등록 실패 - 내용 공백")
    @Test
    void addArticle_fail_when_content_isEmpty() {
        //given
        BasicForm basicForm = new BasicForm();
        basicForm.setArticleType(ArticleType.FEELINGS);
        basicForm.setTitle("테스트");
        basicForm.setContent("");
        basicForm.setPrivacy(false);

        //when & then
        assertThrows(InvalidFormException.class, ()->{
            basicService.addArticle(basicForm.getArticleType(), basicForm.getTitle(), basicForm.getContent(),
                    user, basicForm.isPrivacy());
        });
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void editArticle_success() {
        //given
        BasicArticle originalArticle = new BasicArticle(ArticleType.FEELINGS, "원래 제목", "원래 내용",user,
                LocalDateTime.now(), false);
        basicRepository.save(originalArticle);

        //when
        basicService.editArticle(originalArticle, ArticleType.TRANSCRIPTION, "수정된 제목", "수정된 내용", true);

        //then
        assertThat(originalArticle.getArticleType()).isEqualTo(ArticleType.TRANSCRIPTION);
        assertThat(originalArticle.getTitle()).isEqualTo("수정된 제목");
        assertThat(originalArticle.getContent()).isEqualTo("수정된 내용");
        assertThat(originalArticle.isPrivacy()).isEqualTo(true);
    }

    @DisplayName("게시글 수정 실패 - 제목 공백")
    @Test
    void editArticle_fail_when_title_isEmpty() {
        //given
        BasicArticle originalArticle = new BasicArticle(ArticleType.FEELINGS, "원래 제목", "원래 내용",user,
                LocalDateTime.now(), false);
        basicRepository.save(originalArticle);

        //when & then
        assertThrows(InvalidFormException.class, () ->{
                basicService.editArticle(originalArticle, ArticleType.TRANSCRIPTION, "","수정된 내용", true);
        });
    }

    @DisplayName("게시글 수정 실패 - 내용 공백")
    @Test
    void editArticle_fail_when_content_isEmpty() {
        //given
        BasicArticle originalArticle = new BasicArticle(ArticleType.FEELINGS, "원래 제목", "원래 내용",user,
                LocalDateTime.now(), false);
        basicRepository.save(originalArticle);

        //when & then
        assertThrows(InvalidFormException.class, () ->{
            basicService.editArticle(originalArticle, ArticleType.TRANSCRIPTION, "수정된 제목","", true);
        });
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void deleteArticle_success() {
        //given
        BasicArticle deleteArticle = new BasicArticle(ArticleType.FEELINGS, "삭제될 글","글 내용",user,
                LocalDateTime.now(), false);
        basicRepository.save(deleteArticle);

        //when
        basicService.deleteArticle(deleteArticle);

        //then
        assertThrows(DataNotFoundException.class,() -> {
            basicService.getArticle(deleteArticle.getId());
        });
    }

    @DisplayName("게시글 삭제 실패 - 존재하지 않는 글")
    @Test
    void deleteArticle_fail_when_notFound() {
        //given
        BasicArticle notSavedArticle = new BasicArticle(ArticleType.FEELINGS, "가짜 글","글 내용",user,
                LocalDateTime.now(), false);

        System.out.println(notSavedArticle.getId());

        //when & then
        assertThrows(DataNotFoundException.class, () -> {
            basicService.deleteArticle(notSavedArticle);
        });
    }

    @DisplayName("인기 게시글 가져오기 성공")
    @Test
    void getPopularArticles_success() {
        //given
        BasicArticle articleLikesAre2 = new BasicArticle(ArticleType.FEELINGS, "좋아요2", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikesAre2);
        articleLikesAre2.increaseLike();
        articleLikesAre2.increaseLike();
        System.out.println(articleLikesAre2.getCountLikes());

        BasicArticle articleLikeIs1 = new BasicArticle(ArticleType.FEELINGS, "좋아요1", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikeIs1);
        articleLikeIs1.increaseLike();
        System.out.println(articleLikeIs1.getCountLikes());

        //when
        List<BasicArticle> assortedArticles = this.basicService.getPopularArticles();

        //then
        assertThat(assortedArticles).isNotNull();
        assertThat(assortedArticles.get(0).getTitle()).isEqualTo("좋아요2");
        assertThat(assortedArticles.get(1).getTitle()).isEqualTo("좋아요1");
    }

    @DisplayName("카테고리별 인기 게시글 가져오기 성공")
    @Test
    void getPopularArticlesByArticleType_success() {
        //given
        BasicArticle articleLikesAre2_feelings = new BasicArticle(ArticleType.FEELINGS, "좋아요2_감정일기", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikesAre2_feelings);
        articleLikesAre2_feelings.increaseLike();
        articleLikesAre2_feelings.increaseLike();
        System.out.println(articleLikesAre2_feelings.getCountLikes());

        BasicArticle articleLikeIs1_feelings = new BasicArticle(ArticleType.FEELINGS, "좋아요1_감정일기", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikeIs1_feelings);
        articleLikeIs1_feelings.increaseLike();
        System.out.println(articleLikeIs1_feelings.getCountLikes());

        BasicArticle articleLikesAre2_transcription = new BasicArticle(ArticleType.TRANSCRIPTION, "좋아요2_필사", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikesAre2_transcription);
        articleLikesAre2_transcription.increaseLike();
        articleLikesAre2_transcription.increaseLike();
        System.out.println(articleLikesAre2_transcription.getCountLikes());

        BasicArticle articleLikeIs1_transcription = new BasicArticle(ArticleType.TRANSCRIPTION, "좋아요1_필사", "글 내용", user,
                LocalDateTime.now(), false);
        this.basicRepository.save(articleLikeIs1_transcription);
        articleLikeIs1_transcription.increaseLike();
        System.out.println(articleLikeIs1_transcription.getCountLikes());

        //when
        List<BasicArticle> assortedArticles_feelings = this.basicService.getPopularArticlesByType(ArticleType.FEELINGS);
        List<BasicArticle> assortedArticles_transcription = this.basicService.getPopularArticlesByType(ArticleType.TRANSCRIPTION);

        //then
        assertThat(assortedArticles_feelings).isNotNull();
        assertThat(assortedArticles_feelings.get(0).getTitle()).isEqualTo("좋아요2_감정일기");
        assertThat(assortedArticles_feelings.get(0).getArticleType()).isEqualTo(ArticleType.FEELINGS);
        assertThat(assortedArticles_feelings.get(1).getTitle()).isEqualTo("좋아요1_감정일기");
        assertThat(assortedArticles_feelings.get(1).getArticleType()).isEqualTo(ArticleType.FEELINGS);

        assertThat(assortedArticles_transcription).isNotNull();
        assertThat(assortedArticles_transcription.get(0).getTitle()).isEqualTo("좋아요2_필사");
        assertThat(assortedArticles_transcription.get(0).getArticleType()).isEqualTo(ArticleType.TRANSCRIPTION);
        assertThat(assortedArticles_transcription.get(1).getTitle()).isEqualTo("좋아요1_필사");
        assertThat(assortedArticles_transcription.get(1).getArticleType()).isEqualTo(ArticleType.TRANSCRIPTION);
    }
}