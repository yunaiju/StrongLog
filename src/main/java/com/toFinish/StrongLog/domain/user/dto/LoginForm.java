package com.toFinish.StrongLog.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    private String username;

    private String password;
}
