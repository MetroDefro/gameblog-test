package sparta.gameblog.entity;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    @DisplayName("update() 테스트 성공")
    void test1() {
        // given
        Comment comment1 = fixtureMonkey.giveMeOne(Comment.class);
        Comment comment2 = fixtureMonkey.giveMeOne(Comment.class);

        // when
        comment1.update(comment2);

        // then
        assertEquals(comment1.getComment(), comment2.getComment());
    }
}