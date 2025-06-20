package com.toFinish.StrongLog.domain.home.controller;

import com.toFinish.StrongLog.domain.board.ArticleType;
import com.toFinish.StrongLog.domain.board.entity.BasicArticle;
import com.toFinish.StrongLog.domain.board.service.BasicService;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final UserService userService;
    private final BasicService basicService;

    // 홈 - 상위 게시물 조회
    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if(principal!=null) {
            User user = this.userService.getUser(principal.getName());
            model.addAttribute("nickname",user.getNickname());
        }

        List<BasicArticle> articles = this.basicService.getPopularArticles();
        model.addAttribute("articles", articles);

        return "home";
    }

    // 홈 - 카테고리별 상위 게시물 조회
    @GetMapping("/types/{articleType}")
    public String articlesByType(@PathVariable("articleType") ArticleType articleType, Model model) {
        List<BasicArticle> articles = basicService.getPopularArticlesByType(articleType);
        model.addAttribute("articles",articles);
        model.addAttribute("articleType", articleType);

        return "home";
    }
}
