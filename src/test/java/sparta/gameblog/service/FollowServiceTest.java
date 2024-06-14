package sparta.gameblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sparta.gameblog.dto.request.FollowRequestDto;
import sparta.gameblog.dto.response.FollowResponseDto;
import sparta.gameblog.entity.Follow;
import sparta.gameblog.entity.User;
import sparta.gameblog.exception.BusinessException;
import sparta.gameblog.exception.ErrorCode;
import sparta.gameblog.mapper.FollowMapper;
import sparta.gameblog.repository.FollowRepository;
import sparta.gameblog.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    FollowRepository followRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FollowMapper followMapper;

    User following;
    User follower;
    Follow follow;
    FollowRequestDto requestDto;

    @BeforeEach
    void setUp() {
        following = User.builder().name("name1").email("email1@email.com").password("password1234!!").id(1L).build();
        follower = User.builder().name("name2").email("email2@email.com").password("password1234!!").id(2L).build();
        follow = Follow.builder().followingUser(following).followerUser(follower).build();
        requestDto = new FollowRequestDto();
        requestDto.setFollowingUserId(following.getId());
    }

    @Test
    @DisplayName("follow 테스트")
    void testFollow() {
        // given
        FollowService followService = new FollowService(followRepository, userRepository, followMapper);

        given(followRepository.findByFollowingUserIdAndFollowerUserId(following.getId(), follower.getId()))
                .willReturn(Optional.empty());
        given(userRepository.findById(following.getId())).willReturn(Optional.of(following));
        given(followMapper.toEntity(following, follower)).willReturn(follow);
        given(followRepository.save(follow)).willReturn(follow);

        // when
        FollowResponseDto responseDto = followService.follow(requestDto, follower);

        // then
        assertNotNull(responseDto);
        assertEquals(following.getId(), responseDto.getFollowingUserId());
        assertEquals(follower.getId(), responseDto.getFollowerUserId());
    }

    @Test
    @DisplayName("unfollow 실패 테스트")
    void testUnfollow() {
        // given
        FollowService followService = new FollowService(followRepository, userRepository, followMapper);

        given(followRepository.findByFollowingUserIdAndFollowerUserId(following.getId(), follower.getId()))
                .willReturn(Optional.empty());
        given(userRepository.findById(following.getId())).willReturn(Optional.of(following));

        // when
        // then
        BusinessException exception = assertThrows(BusinessException.class, () -> followService.unfollow(requestDto, follower));
        assertEquals(exception.getMessage(), ErrorCode.USER_NOT_FOUND.getMessage());
    }
}