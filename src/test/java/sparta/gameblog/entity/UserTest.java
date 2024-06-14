package sparta.gameblog.entity;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    @DisplayName("updateProfile() 테스트 성공")
    void test1() {
        // given
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .build();

        User user = new User();
        String name = fixtureMonkey.giveMeOne(String.class);
        String introduction = fixtureMonkey.giveMeOne(String.class);
        String password = fixtureMonkey.giveMeOne(String.class);

        // when
        user.updateProfile(name, introduction, password);

        // then
        assertEquals(name, user.getName());
        assertEquals(introduction, user.getIntroduction());
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("setSnsInfo() 테스트 성공")
    void test2() {
        // given
        User user = new User();
        SnsInfo snsInfo = new SnsInfo();

        // when
        user.setSnsInfo(snsInfo);

        // then
        assertEquals(snsInfo, user.getSnsInfo());
    }
}