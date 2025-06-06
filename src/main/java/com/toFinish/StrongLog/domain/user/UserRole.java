package com.toFinish.StrongLog.domain.user;

import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    UserRole(String value) {this.value = value;}

    private String value;
}
