import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static java.util.Objects.hash;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDeserializationTest {

    public static class User {
        private String username;
        private int age;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(final Object o) {
            return this == o
                || (o instanceof User
                 && hashCode() == o.hashCode());
        }

        @Override
        public int hashCode() {
            return hash(getUsername(),
                        getAge());
        }
    }

    abstract static class UserMixin {
        public static final String USER_NAME = "user_name";
        @JsonProperty
        @JsonAlias(USER_NAME)
        abstract void setUsername(String username);
    }

    @Test
    public void testUserDeserializationWithSingleObjectMapper() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(User.class, UserMixin.class);

        String json1 = "{\"username\": \"Bob\", \"age\": 25}";
        assertThat(json1).doesNotContain(UserMixin.USER_NAME);

        String json2 = json1.replace("username", UserMixin.USER_NAME);
        assertThat(json2).contains(UserMixin.USER_NAME);

        assertNotEquals(json1, json2);

        User user1 = mapper.readValue(json1, User.class);
        User user2 = mapper.readValue(json2, User.class);
        assertEquals(user1, user2);
    }
}
