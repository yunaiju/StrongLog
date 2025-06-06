package com.toFinish.StrongLog.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TranscriptionForm {
    private String book;

    private String page;

    private String content;

    private boolean privacy = false;
}
