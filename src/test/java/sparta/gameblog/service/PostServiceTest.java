package sparta.gameblog.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sparta.gameblog.dto.request.PostCreateRequestDto;
import sparta.gameblog.dto.request.PostPageableRequestDto;
import sparta.gameblog.dto.request.PostUpdateRequestDto;
import sparta.gameblog.dto.request.UserSignupRequestDto;
import sparta.gameblog.dto.response.PostCreateResponseDto;
import sparta.gameblog.dto.response.PostGetResponseDto;
import sparta.gameblog.dto.response.PostUpdateResponseDto;
import sparta.gameblog.dto.response.PostsResponseDto;
import sparta.gameblog.entity.User;
import sparta.gameblog.exception.BusinessException;
import sparta.gameblog.exception.ErrorCode;
import sparta.gameblog.mapper.PostMapper;
import sparta.gameblog.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    final String EMAIL1 = "email3@email.com";
    final String PASSWORD = "pass1234!!";
    final String NAME = "name";

    final String TITLE = "title";
    final String CONTENTS = "contents";

    final String TITLE_MOD = "title mod";
    final String CONTENTS_MOD = "contents mod";

    User user;
    Long postId;

    @BeforeAll
    void setUp() {
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail(EMAIL1);
        requestDto.setPassword(PASSWORD);
        requestDto.setName(NAME);
        user = userService.signup(requestDto);
    }

    @Test
    @DisplayName("createPost 테스트")
    @Order(1)
    void testCreatePost() {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto();
        requestDto.setTitle(TITLE);
        requestDto.setContents(CONTENTS);

        // when
        PostCreateResponseDto responseDto = postService.createPost(requestDto, user);
        postId = responseDto.getId();

        // then
        assertEquals(responseDto.getTitle(), TITLE);
        assertEquals(responseDto.getContents(), CONTENTS);
        assertEquals(responseDto.getUserEmail(), user.getEmail());
    }

    @Test
    @DisplayName("getPost 테스트")
    @Order(2)
    void testGetPost() {
        // given

        // when
        PostGetResponseDto responseDto = postService.getPost(postId);

        // then
        assertEquals(responseDto.getTitle(), TITLE);
        assertEquals(responseDto.getContents(), CONTENTS);
        assertEquals(responseDto.getUserEmail(), user.getEmail());
    }

    @Test
    @DisplayName("getPosts 테스트")
    @Order(3)
    void testGetPosts() {
        // given
        PostPageableRequestDto requestDto = new PostPageableRequestDto();
        requestDto.setPage(0);

        // when
        PostsResponseDto responseDto = postService.getPosts(requestDto);

        // then
        assertEquals(responseDto.getPage(), 0);
        assertEquals(responseDto.getData().get(0).getTitle(), TITLE);
        assertEquals(responseDto.getData().get(0).getContents(), CONTENTS);
        assertEquals(responseDto.getData().get(0).getUserEmail(), user.getEmail());
    }

    @Test
    @DisplayName("updatePost 테스트")
    @Order(4)
    void testUpdatePost() {
        // given
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto();
        requestDto.setTitle(TITLE_MOD);
        requestDto.setContents(CONTENTS_MOD);

        // when
        PostUpdateResponseDto responseDto = postService.updatePost(postId, requestDto, user);

        // then
        assertEquals(responseDto.getTitle(), TITLE_MOD);
        assertEquals(responseDto.getContents(), CONTENTS_MOD);
        assertEquals(responseDto.getUserEmail(), user.getEmail());
    }

    @Test
    @DisplayName("deletePost 테스트")
    @Order(5)
    void testDeletePost() {
        // given

        // when
        postService.deletePost(postId, user);

        // then
        BusinessException exception = assertThrows(BusinessException.class, () -> postService.getPost(postId));
        assertEquals(exception.getMessage(), ErrorCode.POST_NOT_FOUND.getMessage());
    }

}