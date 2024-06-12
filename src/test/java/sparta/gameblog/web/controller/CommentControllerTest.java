package sparta.gameblog.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.context.WebApplicationContext;
import sparta.gameblog.dto.request.CommentCreateRequestDto;
import sparta.gameblog.entity.User;
import sparta.gameblog.security.config.SecurityConfig;
import sparta.gameblog.security.principal.UserPrincipal;
import sparta.gameblog.service.CommentService;
import sparta.gameblog.web.MockSpringSecurityFilter;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
class CommentControllerTest {
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

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
    @DisplayName("createComment 테스트")
    void createComment() throws Exception {
        // given
        mockUserSetup();
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
        requestDto.setComment("comment");

        // when
        ResultActions actions = mockMvc.perform(post("/api/post/1/comment")
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
    @DisplayName("getComment 테스트")
    void getComment() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(get("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("updateComment 테스트")
    void updateComment() throws Exception {
        // given
        mockUserSetup();
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto();
        requestDto.setComment("comment");

        // when
        ResultActions actions = mockMvc.perform(put("/api/comment/1")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("deleteComment 테스트")
    void deleteComment() throws Exception {
        // given
        mockUserSetup();

        // when
        ResultActions actions = mockMvc.perform(delete("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        // then
        actions.andExpect(status().isNoContent())
                .andDo(print());
    }

}