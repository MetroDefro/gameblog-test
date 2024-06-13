package sparta.gameblog.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import sparta.gameblog.dto.request.UserSignupRequestDto;
import sparta.gameblog.entity.User;
import sparta.gameblog.mapper.UserMapper;
import sparta.gameblog.repository.UserRepository;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    final String EMAIL1 = "email1@email.com";
    final String EMAIL2 = "email2@email.com";
    final String PASSWORD = "pass1234!!";
    final String NAME = "name";
    final String SNS_INFO = "google";

    User user;
    User socialUser;

    @Test
    @Order(1)
    @DisplayName("signup 테스트")
    void testSignup() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail(EMAIL1);
        requestDto.setPassword(PASSWORD);
        requestDto.setName(NAME);

        // when
        user = userService.signup(requestDto);

        // then
        assertNotNull(user);
        assertEquals(EMAIL1, user.getEmail());
        assertEquals(NAME, user.getName());
    }

    @Test
    @Order(2)
    @DisplayName("getUserByEmail 테스트")
    void testGetUserByEmail() {
        // given

        // when
        user = userService.getUserByEmail(EMAIL1);

        // then
        assertNotNull(user);
        assertEquals(EMAIL1, user.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("signup 소셜 테스트")
    void testSignupSocial() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail(EMAIL2);
        requestDto.setPassword(PASSWORD);
        requestDto.setName(NAME);

        // when
        socialUser = userService.signup(requestDto, SNS_INFO);

        // then
        assertNotNull(socialUser);
        assertEquals(EMAIL2, socialUser.getEmail());
        assertEquals(NAME, socialUser.getName());
    }

    @Test
    @Order(4)
    @DisplayName("getUserByEmailWithSnsInfo 테스트")
    void testGetUserByEmailWithSnsInfo() {
        // given
        Supplier<? extends RuntimeException> supplier = () -> new OAuth2AuthenticationException("존재하지 않는 유저");

        // when
        socialUser = userService.getUserByEmailWithSnsInfo(EMAIL2, supplier);

        // then
        assertNotNull(socialUser);
        assertEquals(EMAIL2, socialUser.getEmail());
    }


    @Test
    @Order(5)
    @DisplayName("verify 테스트")
    void testVerify() {
        // given

        // when
        userService.verify(user);

        // then
        assertEquals(user.getStatusCode(), User.StatusCode.ACTIVE);
    }

    @Test
    @Order(6)
    @DisplayName("addSnsInfoToUser 테스트")
    void testAddSnsInfoToUser() {
        // given

        // when
        userService.addSnsInfoToUser(user, SNS_INFO);

        // then
        assertNotNull(user.getSnsInfo());
    }

}