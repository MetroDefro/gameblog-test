package sparta.gameblog.entity;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostTest {

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    @DisplayName("update() 테스트 성공")
    void test1() {
        // given
        Post post = new Post();
        Post post2 = fixtureMonkey.giveMeOne(Post.class);

        // when
        post.update(post2);

        // then
        assertEquals(post.getTitle(), post2.getTitle());
        assertEquals(post.getContents(), post2.getContents());
    }

    @Test
    @DisplayName("addComment() 테스트 성공")
    void test3() {
        // given
        Comment comment = fixtureMonkey.giveMeOne(Comment.class);
        Post post = fixtureMonkey.giveMeOne(Post.class);

        // when
        post.addComment(comment);

        // then
        assertTrue(post.getComments().contains(comment));
    }
}