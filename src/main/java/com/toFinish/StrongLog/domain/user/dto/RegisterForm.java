package com.toFinish.StrongLog.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @Size(min=3, max=25)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "영문자, 숫자, 밑줄(_)만 가능합니다 !")
    private String username;

    @Size(min = 8, max = 20)  // 길이 제한
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다 !"
    )
    private String password;

    private String passwordCheck;

    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "공백과 특수문자는 불가능합니다 !")
    private String nickname;
}
