package com.toFinish.StrongLog.domain.board.dto;

import com.toFinish.StrongLog.domain.board.ArticleType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicForm {
    
    private ArticleType articleType;

    @NotEmpty(message = "내용을 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요.")
    private String content;

    private boolean privacy;
}
