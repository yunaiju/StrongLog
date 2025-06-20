package com.toFinish.StrongLog.domain.board.controller;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.dto.BasicForm;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.service.ArticleLikeService;
import com.toFinish.StrongLog.domain.board.service.BasicService;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.comment.service.CommentService;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BasicController {
    private final BasicService basicService;
    private final UserService userService;
    private final CommentService commentService;
    private final ArticleLikeService articleLikeService;

    // 전체 게시물 조회
    @GetMapping("/{username}/blog")
    public String articles(@PathVariable("username") String username, Model model, Principal principal,
                           @RequestParam(value = "page", defaultValue = "0") int page) {
        User author = this.userService.getUser(username);

        if(principal != null && username.equals(principal.getName())) {
            Page<BasicArticle> articles = this.basicService.getArticlesByAuthor(page, author);
            model.addAttribute("articles", articles);
        } else {
            Page<BasicArticle> articles = this.basicService.getPublicArticleByAuthor(page, author);
            model.addAttribute("articles", articles);
        }
        model.addAttribute("nickname",author.getNickname());

        return "board/basicReadList";
    }

    // 게시물 등록
    @GetMapping("/{username}/blog/{articleType}/add")
    public String addArticle(@PathVariable("username") String username, @PathVariable("articleType") ArticleType articleType,
                             BasicForm basicForm) {
        basicForm.setArticleType(articleType);
        return "board/basicAddForm";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{username}/blog/{articleType}/add")
    public String addArticle(@PathVariable("username")String username, @PathVariable("articleType") ArticleType articleType,
                             @Valid BasicForm basicForm, BindingResult bindingResult,
                             Principal principal, Model model) {
        if(bindingResult.hasErrors()) return "board/basicAddForm";

        User user = this.userService.getUser(principal.getName());
        model.addAttribute("user",user);
        Long id = this.basicService.addArticle(basicForm.getArticleType(), basicForm.getTitle(), basicForm.getContent(), user, basicForm.isPrivacy());

        return "redirect:/{username}/blog/"+id;
    }

    // 카테고리별 전체 게시물 조회
    @GetMapping("/{username}/blog/types/{articleType}")
    public String articlesByType(@PathVariable("username")String username, @PathVariable("articleType") ArticleType articleType
                             , Model model, Principal principal, @RequestParam(value="page",defaultValue = "0")int page) {
        User author = this.userService.getUser(username);

        if(principal != null && username.equals(principal.getName())) {
            Page<BasicArticle> articles = this.basicService.getArticlesByAuthorAndArticleType(page, author, articleType);
            model.addAttribute("articles", articles);
        } else {
            Page<BasicArticle> articles = this.basicService.getPublicArticlesByAuthorAndArticleType(page, author, articleType);
            model.addAttribute("articles", articles);
        }

        model.addAttribute("articleType", articleType);
        model.addAttribute("nickname",author.getNickname());

        return "board/basicReadList";
    }

    // 개별 게시물 조회
    @GetMapping("/{username}/blog/{id}")
    public String article(@PathVariable("username")String username, @PathVariable("id")Long id, Model model,
                          @Nullable Principal principal) {
        BasicArticle article = this.basicService.getArticle(id);
        List<Comment> commentList = this.commentService.getCommentList(article);

        model.addAttribute("article",article);
        model.addAttribute("commentList", commentList);

        User user = null;
        if (principal != null) {
            user = userService.getUser(principal.getName());
            model.addAttribute("isLike",this.articleLikeService.isLike(user, article));
        }

        User author = this.userService.getUser(username);
        model.addAttribute("nickname",author.getNickname());

        return "board/basicRead";
    }
    // 좋아요
    @PostMapping("/{username}/blog/{id}/like")
    public String likePost(@PathVariable("username") String username, @PathVariable("id") Long id, Principal principal,
                           Model model) {
        User user = this.userService.getUser(principal.getName());
        articleLikeService.likePost(user, basicService.getArticle(id));

        User author = this.userService.getUser(username);
        model.addAttribute("nickname",author.getNickname());

        return "redirect:/{username}/blog/{id}";
    }
    // 좋아요 취소
    @PostMapping("/{username}/blog/{id}/unlike")
    public String unLikePost(@PathVariable("username") String username, @PathVariable("id") Long id, Principal principal,
                             Model model) {
        User user = this.userService.getUser(principal.getName());
        articleLikeService.unlikePost(user, basicService.getArticle(id));

        User author = this.userService.getUser(username);
        model.addAttribute("nickname",author.getNickname());

        return "redirect:/{username}/blog/{id}";
    }


    // 개별 게시물 수정
    @GetMapping("/{username}/blog/{id}/edit")
    public String editArticle(@PathVariable("username")String username, @PathVariable("id")Long id,
                              BasicForm basicForm, Principal principal) {
        BasicArticle article = this.basicService.getArticle(id);
        if(!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        basicForm.setArticleType(article.getArticleType());
        basicForm.setTitle(article.getTitle());
        basicForm.setContent(article.getContent());
        basicForm.setPrivacy(article.isPrivacy());

        return "board/basicAddForm";
    }
    @PostMapping("/{username}/blog/{id}/edit")
    public String editArticle(@PathVariable("username")String username, @PathVariable("id")Long id,
                              @Valid BasicForm basicForm, BindingResult bindingResult, Principal principal,
                              Model model) {
        if(bindingResult.hasErrors()) return "board/basicAddForm";
        BasicArticle article = this.basicService.getArticle(id);

        if(!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }

        this.basicService.editArticle(article, basicForm.getArticleType(), basicForm.getTitle(), basicForm.getContent(), basicForm.isPrivacy());

        User author = this.userService.getUser(username);
        model.addAttribute("nickname",author.getNickname());

        return "redirect:/{username}/blog/{id}";
    }

    // 개별 게시물 삭제
    @PostMapping("/{username}/blog/{id}/delete")
    public String deleteArticle(@PathVariable("username")String username, @PathVariable("id")Long id, Model model) {
        BasicArticle article = this.basicService.getArticle(id);

        User author = this.userService.getUser(username);
        model.addAttribute("nickname",author.getNickname());

        this.basicService.deleteArticle(article);
        return "redirect:/{username}/blog";
    }
}
