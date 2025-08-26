package com.toFinish.StrongLog.domain.user.service;

import com.toFinish.StrongLog.domain.global.exception.DuplicateNicknameException;
import com.toFinish.StrongLog.domain.global.exception.DuplicateUsernameException;
import com.toFinish.StrongLog.domain.global.exception.PasswordMismatchException;
import com.toFinish.StrongLog.domain.user.dto.RegisterForm;
import com.toFinish.StrongLog.domain.user.entity.User;
import com.toFinish.StrongLog.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @BeforeEach // 테스트 케이스 시작 전마다 실행
    public void beforeEach(){
        userRepository.deleteAll();
    }

    @DisplayName("회원 조회 성공")
    @Test
    void getUser_success() {
        //given
        userRepository.save(new User("test","test1234!","테스트씨"));

        // when
        User getUser = userService.getUser("test");


        // then
        Assertions.assertThat(getUser.getUsername()).isEqualTo("test");
    }

    @DisplayName("회원 조회 실패")
    @Test
    void getUser_fail() {
        //given
        userRepository.save(new User("test","test1234!","테스트씨"));

        // when & then
        assertThrows(ResponseStatusException.class, () -> {
            userService.getUser("t");
        });
    }

    @DisplayName("회원가입 성공")
    @Test
    void register_success() {
        //given
        RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("test");
        registerForm.setPassword("test1234!");
        registerForm.setPasswordCheck("test1234!");
        registerForm.setNickname("테스트씨");

        //when
        userService.createUser(registerForm.getUsername(), registerForm.getPassword(),
                registerForm.getPasswordCheck(), registerForm.getNickname());
        User getUser = userService.getUser("test");

        //then
        Assertions.assertThat(getUser.getUsername()).isEqualTo(registerForm.getUsername());
        Assertions.assertThat(getUser.getNickname()).isEqualTo(registerForm.getNickname());
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    void register_fail_when_username_duplicate() {
        //given
        User d_user = new User("d_username","test1234!","중복테스트씨1");
        userRepository.save(d_user);

        RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("d_username");
        registerForm.setPassword("test1234!");
        registerForm.setPasswordCheck("test1234!");
        registerForm.setNickname("중복테스트씨2");

        // when & then
        assertThrows(DuplicateUsernameException.class, () -> {
            userService.createUser(registerForm.getUsername(), registerForm.getPassword(),
                    registerForm.getPasswordCheck(), registerForm.getNickname());
        });
    }

    @DisplayName("회원가입 실패 - 비밀번호 확인 불일치")
    @Test
    void register_fail_when_password_incorrect() {
        //given
        RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("test");
        registerForm.setPassword("test1234!");
        registerForm.setPasswordCheck("test1234");
        registerForm.setNickname("비번테스트씨");

        // when & then
        assertThrows(PasswordMismatchException.class, () -> {
            userService.createUser(registerForm.getUsername(), registerForm.getPassword(),
                    registerForm.getPasswordCheck(), registerForm.getNickname());
        });
    }

    @DisplayName("회원가입 실패 - 닉네임 중복")
    @Test
    void register_fail_when_nickname_duplicate() {
        //given
        User d_user = new User("test1","test1234!","중복테스트씨");
        userRepository.save(d_user);

        RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername("test2");
        registerForm.setPassword("test1234!");
        registerForm.setPasswordCheck("test1234!");
        registerForm.setNickname("중복테스트씨");

        // when & then
        assertThrows(DuplicateNicknameException.class, () -> {
            userService.createUser(registerForm.getUsername(), registerForm.getPassword(),
                    registerForm.getPasswordCheck(), registerForm.getNickname());
        });
    }
}