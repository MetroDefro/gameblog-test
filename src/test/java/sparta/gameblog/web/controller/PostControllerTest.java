package sparta.gameblog.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import sparta.gameblog.dto.request.PostCreateRequestDto;
import sparta.gameblog.dto.request.PostPageableRequestDto;
import sparta.gameblog.dto.response.PostsResponseDto;
import sparta.gameblog.entity.User;
import sparta.gameblog.security.config.SecurityConfig;
import sparta.gameblog.security.principal.UserPrincipal;
import sparta.gameblog.service.FollowService;
import sparta.gameblog.service.PostService;
import sparta.gameblog.smtp.service.SmtpService;
import sparta.gameblog.web.MockSpringSecurityFilter;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class PostControllerTest {
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @MockBean
    FollowService followService;

    @MockBean
    SmtpService smtpService;

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        String username = "user";
        String password = "password1234!!";
        String email = "user@email.com";
        User.Role role = User.Role.NORMAL;
        User.StatusCode statusCode = User.StatusCode.ACTIVE;
        User user = User.builder()
                .email(email)
                .name(username)
                .password(password)
                .role(role)
                .statusCode(statusCode)
                .build();

        UserPrincipal userPrincipal = new UserPrincipal(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(userPrincipal, password, null);
    }

    @Test
    @DisplayName("createPost 테스트")
    void testCreatePost() throws Exception {
        // given
        mockUserSetup();
        PostCreateRequestDto requestDto = new PostCreateRequestDto();
        requestDto.setTitle("title");
        requestDto.setContents("contents");

//        PostCreateRequestDto requestDto = fixtureMonkey.giveMeOne(PostCreateRequestDto.class);
//        System.out.println(requestDto.getContents());

        // when
        ResultActions actions = mockMvc.perform(post("/api/post")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("getPost 테스트")
    void testGetPost() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(get("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("getPosts 테스트")
    void testGetPosts() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        PostsResponseDto responseDto = PostsResponseDto.builder().totalElements(100L).page(1).build();
        when(postService.getPosts(any(PostPageableRequestDto.class))).thenReturn(responseDto);

        // when
        ResultActions actions = mockMvc.perform(get("/api/post")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("getFollowingPosts 테스트")
    void testGetFollowingPosts() throws Exception {
        // given
        mockUserSetup();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        // when
        ResultActions actions = mockMvc.perform(get("/api/post/following")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("deletePost 테스트")
    void testDeletePost() throws Exception {
        // given
        mockUserSetup();

        // when
        ResultActions actions = mockMvc.perform(delete("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("updatePost 테스트")
    void testUpdatePost() throws Exception {
        // given
        mockUserSetup();
        PostCreateRequestDto requestDto = new PostCreateRequestDto();
        requestDto.setTitle("title");
        requestDto.setContents("contents");

        // when
        ResultActions actions = mockMvc.perform(put("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("updatePost 실패 테스트 - title, contents 비어있음.")
    void testUpdatePostFail() throws Exception {
        // given
        mockUserSetup();
        PostCreateRequestDto requestDto = new PostCreateRequestDto();

        // when
        ResultActions actions = mockMvc.perform(put("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isBadRequest())
                .andDo(print());
    }
}