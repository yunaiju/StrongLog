package com.toFinish.StrongLog.domain.user.controller;

import com.toFinish.StrongLog.domain.global.exception.DuplicateNicknameException;
import com.toFinish.StrongLog.domain.global.exception.DuplicateUsernameException;
import com.toFinish.StrongLog.domain.global.exception.PasswordMismatchException;
import com.toFinish.StrongLog.domain.user.dto.RegisterForm;
import com.toFinish.StrongLog.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false)String error, Model model) {
        if(error != null) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return "user/loginForm";
    }

    @GetMapping("/register")
    public String register(RegisterForm registerForm) {
        return "user/registerForm";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterForm registerForm, BindingResult bindingResult) {

        try {
            userService.createUser(registerForm.getUsername(), registerForm.getPassword(), registerForm.getPasswordCheck(),
                    registerForm.getNickname());
        } catch (DuplicateUsernameException e) {
            bindingResult.rejectValue("username","duplicate",e.getMessage());
        } catch (PasswordMismatchException e) {
            bindingResult.rejectValue("passwordCheck","mismatch",e.getMessage());
        } catch (DuplicateNicknameException e) {
            bindingResult.rejectValue("nickname","duplicate", e.getMessage());
        }

        if(bindingResult.hasErrors()) return "user/registerForm";

        return "redirect:/";
    }

}
