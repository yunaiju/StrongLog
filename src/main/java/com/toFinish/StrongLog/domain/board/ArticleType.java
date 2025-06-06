package com.toFinish.StrongLog.domain.board;

import java.util.List;

public enum ArticleType {
    FEELINGS("감정일기"), THANKS("감사일기"), SUGGESTION("자기암시"), SAYING("명언"), TRANSCRIPTION("필사"), DREAM("꿈일기");

    private final String displayName;

    ArticleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
