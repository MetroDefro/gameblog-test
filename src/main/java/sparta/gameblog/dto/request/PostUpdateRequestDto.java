package sparta.gameblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostUpdateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

}
