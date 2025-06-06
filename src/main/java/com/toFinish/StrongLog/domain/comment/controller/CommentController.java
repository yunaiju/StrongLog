package com.toFinish.StrongLog.domain.comment.controller;

import com.toFinish.StrongLog.domain.board.dto.BasicForm;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.service.BasicService;
import com.toFinish.StrongLog.domain.comment.dto.CommentForm;
import com.toFinish.StrongLog.domain.comment.entity.Comment;
import com.toFinish.StrongLog.domain.comment.service.CommentService;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.service.UserService;
import jakarta.persistence.Basic;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.tokens.CommentToken;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;
    private final BasicService basicService;
    private final UserService userService;

    @PostMapping("/{id}/comment/add")
    public String addComment(@PathVariable("id") Long id,
                             @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) return "board/basicAddForm";
        User user = this.userService.getUser(principal.getName());
        BasicArticle article = this.basicService.getArticle(id);

        this.commentService.addComment(commentForm.getContent(), user, article);

        String username = article.getAuthor().getUsername().toString();
        return "redirect:/"+username+"/"+id;
    }

    @PostMapping("/{id}/comment/{commentId}/edit")
    public String editComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId,
                              @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) return "board/basicRead";

        BasicArticle article = this.basicService.getArticle(id);
        Comment comment = this.commentService.getComment(commentId);

        if(!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }

        this.commentService.editComment(comment, commentForm.getContent());

        String username = article.getAuthor().getUsername().toString();
        return "redirect:/"+username+"/"+id;
    }

    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId, Principal principal) {
        BasicArticle article = this.basicService.getArticle(id);
        Comment comment = this.commentService.getComment(commentId);

        if(!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
        }

        this.commentService.deleteComment(comment);

        String username = article.getAuthor().getUsername().toString();
        return "redirect:/"+username+"/"+id;
    }
}
