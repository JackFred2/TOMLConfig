package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.annotations.Transitive;
import red.jackf.tomlconfig.reflections.mapping.util.OffsetDateTimeMapping;

import java.time.OffsetDateTime;
import java.util.*;

public class ExampleConfig implements Config {
    public List<User> users = new ArrayList<>();
    {
        users.add(new User(34L, "br", 3, 3, 4));
        users.add(new User(12L, "uh", 1, 1200, -32));
    }

    public User master = new User(0L, "0");

    public OffsetDateTime time = OffsetDateTime.now();

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return userId.equals(user.userId) && username.equals(user.username) && posts.equals(user.posts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, username, posts);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleConfig that = (ExampleConfig) o;
        return hash == that.hash && users.equals(that.users) && master.equals(that.master);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, master, hash);
    }
}
