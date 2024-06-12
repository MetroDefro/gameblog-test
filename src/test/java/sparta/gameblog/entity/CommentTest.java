package sparta.gameblog.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("update() 테스트 성공")
    void test1() {
        // given
        Comment comment1 = Comment.builder().comment("comment1").build();
        Comment comment2 = Comment.builder().comment("comment2").build();

        // when
        comment1.update(comment2);

        // then
        assertEquals(comment1.getComment(), comment2.getComment());
    }
}