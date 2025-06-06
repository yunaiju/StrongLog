package com.toFinish.StrongLog.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentForm {
    @NotEmpty(message = "내용을 입력하세요.")
    private String content;
}
