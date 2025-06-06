package com.toFinish.StrongLog.domain.global.controller;

import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class BaseController {
    private final UserService userService;
    @ModelAttribute
    public void addUserToModel(Model model, @PathVariable(required = false) String username) {
        if (username != null) {
            model.addAttribute("username", username);
            User user = this.userService.getUser(username);
            model.addAttribute("nickname", user.getNickname());
        }
    }
}
