package sparta.gameblog.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sparta.gameblog.MySQLTestContainer;
import sparta.gameblog.dto.request.CommentCreateRequestDto;
import sparta.gameblog.dto.request.PostCreateRequestDto;
import sparta.gameblog.dto.request.UserSignupRequestDto;
import sparta.gameblog.dto.response.CommentCreateResponseDto;
import sparta.gameblog.entity.User;
import sparta.gameblog.exception.BusinessException;
import sparta.gameblog.exception.ErrorCode;
import sparta.gameblog.mapper.CommentMapper;
import sparta.gameblog.repository.CommentRepository;
import sparta.gameblog.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceTest extends MySQLTestContainer {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    final String EMAIL1 = "email4@email.com";
    final String PASSWORD = "pass1234!!";
    final String NAME = "name";

    final String TITLE = "title";
    final String CONTENTS = "contents";

    final String COMMENT = "comment";
    final String COMMENT_MOD = "comment mod";

    User user;
    Long postId;
    Long commentId;

    @BeforeAll
    void setUp() {
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail(EMAIL1);
        userSignupRequestDto.setPassword(PASSWORD);
        userSignupRequestDto.setName(NAME);
        user = userService.signup(userSignupRequestDto);

        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setTitle(TITLE);
        postCreateRequestDto.setContents(CONTENTS);

        postId = postService.createPost(postCreateRequestDto, user).getId();
    }

    @AfterAll
    void tearDown() {
        postService.deletePost(postId, user);
    }

    @Test
    @DisplayName("createComment 테스트")
    @Order(1)
    void testCreateComment() {
        // given
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
        requestDto.setComment(COMMENT);

        // when
        CommentCreateResponseDto responseDto = commentService.createComment(requestDto, postId, user);
        commentId = responseDto.getId();

        // then
        assertEquals(COMMENT, responseDto.getComment());
        assertEquals(user.getId(), responseDto.getUserId());
    }

    @Test
    @DisplayName("getComment 테스트")
    @Order(2)
    void testGetComment() {
        // given

        // when
        CommentCreateResponseDto responseDto = commentService.getComment(commentId);

        // then
        assertEquals(COMMENT, responseDto.getComment());
        assertEquals(user.getId(), responseDto.getUserId());
    }

    @Test
    @DisplayName("updateComment 테스트")
    @Order(3)
    void testUpdateComment() {
        // given
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
        requestDto.setComment(COMMENT_MOD);

        // when
        CommentCreateResponseDto responseDto = commentService.updateComment(commentId, requestDto, user);

        // then
        assertEquals(COMMENT_MOD, responseDto.getComment());
        assertEquals(user.getId(), responseDto.getUserId());
    }

    @Test
    @DisplayName("deleteComment 테스트")
    @Order(4)
    void testDeleteComment() {
        // given

        // when
        commentService.deleteComment(commentId);

        // then
        BusinessException exception = assertThrows(BusinessException.class, () -> commentService.getComment(commentId));
        assertEquals(exception.getMessage(), ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }
}