package sparta.gameblog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import sparta.gameblog.dto.LoginTokenDto;
import sparta.gameblog.jwt.JwtUtil;
import sparta.gameblog.security.principal.UserPrincipal;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LoginTokenDto token = LoginTokenDto.builder()
                .accessToken(jwtUtil.createAccessToken(((UserPrincipal)authentication.getPrincipal()).getUser()))
                .build();

        // JSON 형태로 토큰을 응답으로 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));
    }
}
