package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;
import red.jackf.tomlconfig.annotations.Comment;
import red.jackf.tomlconfig.annotations.Transitive;

import java.time.OffsetDateTime;
import java.util.*;

public class ExampleConfig implements Config {
    public List<User> users = new ArrayList<>();
    {
        users.add(new User(34L, "br", new User.Props(false),3, 3, 4));
        users.add(new User(12L, "uh", new User.Props(false),1, 1200, -32));
        users.add(new User(1L, "test", new User.Props(false),1, 1200, -32));
    }

    public User master = new User(0L, "0", new User.Props(true),43, 653);

    @Comment(value = "Timestamp of config creation")
    public OffsetDateTime time = OffsetDateTime.now();

    long hash = 348497234L;

    @Transitive
    public static class User {
        private Long userId;
        private String username;
        private List<Integer> posts;

        @Comment(value = "List of user properties; marks this user as admin.")
        private Props props;

        public User() {}

        public User(Long userId, String username, Props props, Integer... posts) {
            this.userId = userId;
            this.username = username;
            this.posts = Arrays.asList(posts);
            this.props = props;
        }

        @Override
        public String toString() {
            return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", posts=" + posts +
                ", props=" + props +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(posts, user.posts) && Objects.equals(props, user.props);
        }

        @Transitive
        public static class Props {
            private boolean admin = false;

            public Props() {}

            public Props(boolean admin) {
                this.admin = admin;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Props props = (Props) o;
                return admin == props.admin;
            }

            @Override
            public int hashCode() {
                return Objects.hash(admin);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, username, posts);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleConfig that = (ExampleConfig) o;
        return hash == that.hash && Objects.equals(users, that.users) && Objects.equals(master, that.master) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, master, time, hash);
    }
}
