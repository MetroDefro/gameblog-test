package sparta.gameblog.mapper;

import org.springframework.stereotype.Component;
import sparta.gameblog.dto.PostCreateRequestDto;
import sparta.gameblog.dto.PostCreateResponseDto;
import sparta.gameblog.entity.Post;

@Component
public class PostMapper {
    public Post toEntity(PostCreateRequestDto requestDto) {
        return Post.builder()
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .build();
    }

    public PostCreateResponseDto toPostCreateResponseDto(Post post) {
        return new PostCreateResponseDto(post);
    }
}
