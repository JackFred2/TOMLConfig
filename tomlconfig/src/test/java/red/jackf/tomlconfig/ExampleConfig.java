package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.annotations.Transitive;

import java.util.*;

public class ExampleConfig implements Config {
    public List<User> users = new ArrayList<>();
    {
        users.add(new User(34L, "br", 3, 3, 4));
        users.add(new User(12L, "uh", 1, 1200, -32));
    }

    public User master = new User(0L, "0");

    long hash = 348497234L;

    @Transitive
    public static class User {
        private Long userId;
        private String username;
        private List<Integer> posts;

        public User() {}

        public User(Long userId, String username, Integer... posts) {
            this.userId = userId;
            this.username = username;
            this.posts = Arrays.asList(posts);
        }

        @Override
        public String toString() {
            return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", posts=" + posts +
                '}';
        }
    }

    @Override
    public String toString() {
        return "ExampleConfig{" +
            "users=" + users +
            ", master=" + master +
            ", hash=" + hash +
            '}';
    }
}
