package com.toFinish.StrongLog.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SayingForm {
    private String content;

    private String source;

    private boolean privacy = false;
}
