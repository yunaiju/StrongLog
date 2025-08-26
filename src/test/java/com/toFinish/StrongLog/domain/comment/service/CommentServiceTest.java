package com.toFinish.StrongLog.domain.comment.service;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.repository.BasicRepository;
import com.toFinish.StrongLog.domain.comment.dto.CommentForm;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.comment.repository.CommentRepository;
import com.toFinish.StrongLog.domain.global.exception.DataNotFoundException;
import com.toFinish.StrongLog.domain.global.exception.InvalidFormException;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentService commentService;
    @Autowired private BasicRepository basicRepository;
    @Autowired private UserRepository userRepository;

    private User user1;
    private BasicArticle article1;

    @BeforeEach
    public void beforeEach() {
        this.commentRepository.deleteAll();
        this.basicRepository.deleteAll();
        this.userRepository.deleteAll();

        user1 = new User("test1","test1234!","테스트씨");
        this.userRepository.save(user1);

        article1 = new BasicArticle(ArticleType.FEELINGS, "테스트글1","글 내용"
                ,user1, LocalDateTime.now(),false);
        this.basicRepository.save(article1);
    }

    @DisplayName("댓글 가져오기 성공")
    @Test
    void getComment_success() {
        //given
        Comment comment = new Comment("댓글", user1, article1, LocalDateTime.now());
        this.commentRepository.save(comment);

        //when
        Comment getComment = this.commentService.getComment(comment.getId());

        //then
        assertThat(getComment.getContent()).isEqualTo("댓글");
        assertThat(getComment.getAuthor()).isEqualTo(user1);
        assertThat(getComment.getArticle()).isEqualTo(article1);
    }

    @DisplayName("댓글 가져오기 실패")
    @Test
    void getComment_fail() {
        // given
        Long notExistId = -1L;

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.commentService.getComment(notExistId);
        });
    }

    @DisplayName("댓글 추가 성공")
    @Test
    void addComment_success() {
        // given
        CommentForm commentForm = new CommentForm();
        commentForm.setContent("댓글");

        // when
        Comment getComment = this.commentService.addComment(commentForm.getContent(), user1, article1);

        // then
        assertThat(getComment.getContent()).isEqualTo("댓글");
    }

    @DisplayName("댓글 추가 실패 - 내용 공백")
    @Test
    void addComment_fail() {
        // given
        CommentForm commentForm = new CommentForm();
        commentForm.setContent("");

        // when & then
        assertThrows(InvalidFormException.class, ()->{
            this.commentService.addComment(commentForm.getContent(), user1, article1);
        });
    }

    @DisplayName("댓글 수정 성공")
    @Test
    void editComment_success() {
        // given
        Comment comment = new Comment("내용", user1, article1, LocalDateTime.now());
        this.commentRepository.save(comment);

        // when
        this.commentService.editComment(comment, "수정된 내용");

        // then
        assertThat(comment.getContent()).isEqualTo("수정된 내용");
    }

    @DisplayName("댓글 수정 실패")
    @Test
    void editComment_fail() {
        // given
        Comment comment = new Comment("내용", user1, article1, LocalDateTime.now());
        this.commentRepository.save(comment);

        // when & then
        assertThrows(InvalidFormException.class, ()->{
            this.commentService.editComment(comment, "");
        });
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    void deleteComment_success() {
        // given
        Comment deleteComment = new Comment("내용", user1, article1, LocalDateTime.now());
        this.commentRepository.save(deleteComment);

        // when
        this.commentService.deleteComment(deleteComment);

        // then
        assertThrows(DataNotFoundException.class, ()->{
            this.commentService.getComment(deleteComment.getId());
        });
    }

    @DisplayName("댓글 삭제 실패 - 존재하지 않는 댓글")
    @Test
    void deleteComment_fail() {
        // given
        Comment notSavedComment = new Comment("내용", user1, article1, LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, ()-> {
            this.commentService.deleteComment(notSavedComment);
        });
    }

    @DisplayName("댓글 목록 가져오기 성공")
    @Test
    void getCommentList_success() {
        // given
        User user2 = new User("user2", "user2", "user2");
        this.userRepository.save(user2);

        BasicArticle article2 = new BasicArticle(ArticleType.FEELINGS, "글2", "글내용", user2
                , LocalDateTime.now(), false);
        this.basicRepository.save(article2);

        Comment comment1 = new Comment("user1이 작성한 article1의 댓글", user1, article1, LocalDateTime.now());
        this.commentRepository.save(comment1);

        Comment comment2 = new Comment("user2가 작성한 article1의 댓글", user2, article1, LocalDateTime.now());
        this.commentRepository.save(comment2);

        Comment comment3 = new Comment("user1이 작성한 article2의 댓글", user1, article2, LocalDateTime.now());
        this.commentRepository.save(comment3);

        // when
        List<Comment> commentList = this.commentService.getCommentList(article1);

        // then
        assertThat(commentList).isNotNull();
        assertThat(commentList).hasSize(2);
    }
}