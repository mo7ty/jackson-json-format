import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static java.util.Objects.hash;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String json = "{\"username\": \"Bob\", \"age\": 25}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(User.class, UserMixin.class);

        User user1 = mapper.readValue(json, User.class);
        User user2 = mapper.readValue(json.replace("username", UserMixin.USER_NAME), User.class);

        assertEquals(user1, user2);
    }
}
