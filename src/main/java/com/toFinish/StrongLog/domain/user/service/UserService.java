package com.toFinish.StrongLog.domain.user.service;

import com.toFinish.StrongLog.domain.global.exception.DuplicateNicknameException;
import com.toFinish.StrongLog.domain.global.exception.DuplicateUsernameException;
import com.toFinish.StrongLog.domain.global.exception.PasswordMismatchException;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUser(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found"));
    }

    public void createUser(String username, String password, String passwordCheck, String nickname) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("이미 사용중인 아이디입니다.");
        }

        if(!password.equals(passwordCheck)) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException("이미 사용중인 닉네임입니다.");
        }

        User user = new User(username, passwordEncoder.encode(password), nickname);
        this.userRepository.save(user);
    }


}
