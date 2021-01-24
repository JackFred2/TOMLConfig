package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Example config of various features.
 */
public class ExampleConfig implements Config {
    public int version = 2;

    public OffsetDateTime generatedTime = OffsetDateTime.now();

    public Mode mode = Mode.BETA;

    public char character = 'x';
    public double double$Val = 6e+51;
    public String testStr = "long long long \n" +
                            "stringy string \n" +
                            "with newlines";

    // Transient fields do not get saved
    public transient boolean setterCalled = false;
    public void setTestStr(String newVal) {
        System.out.println("Changed testStr!");
        setterCalled = true;
        testStr = newVal;
    }

    public Set<Integer> flags = new HashSet<>();
    {
        flags.add(1);
        flags.add(4);
        flags.add(-54);
        flags.add(2137864);
    }

    @Comment("This is a set of client options, but really to test the writer's max line length option Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam tempor tellus dignissim, efficitur velit in, molestie risus. Phasellus rhoncus augue ipsum, non bibendum arcu pellentesque nec. Praesent rutrum vulputate vehicula. Etiam gravida, risus vel viverra elementum, lorem justo viverra arcu, at tristique eros odio in nisi.")
    public Client client = new Client();

    @Comment("Comments can be applied to fields, and will be printed to the config file.")
    public List<User> users = new ArrayList<>();
    {
        users.add(new User(0, "root", new Group(0, "sudoers"), new Group(1067, "docker")));
        users.add(new User(1055, "apache2", new Group(1091, "apache2")));
    }

    @Transitive // Lets TOMLConfig know that it does not need a special mapping.
    public static class User {
        public int id;
        public String username;
        public List<Group> groups;
        public Options options = new Options();

        public User() {} // A zero-arg constructor is required for deserialization.

        public User(int id, String username, Group... groups) {
            this.id = id;
            this.username = username;
            this.groups = Arrays.asList(groups);
        }

        @Transitive
        public static class Options {
            public boolean admin = false;
            public String address = "192.168.0.1";

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Options options = (Options) o;
                return admin == options.admin && Objects.equals(address, options.address);
            }

            @Override
            public int hashCode() {
                return Objects.hash(admin, address);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return id == user.id && Objects.equals(username, user.username) && Objects.equals(groups, user.groups) && Objects.equals(options, user.options);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, username, groups, options);
        }
    }

    @Transitive
    public static class Group {
        public int id;
        public String groupName;

        public Group() {}

        public Group(int id, String groupName) {
            this.id = id;
            this.groupName = groupName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Group group = (Group) o;
            return id == group.id && Objects.equals(groupName, group.groupName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, groupName);
        }
    }

    @Transitive
    public static class Client {
        public boolean renderLikeThis = false;
        @Comment("Same long comment as above, but really to test the writer's max line length at indent Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam tempor tellus dignissim, efficitur velit in, molestie risus. Phasellus rhoncus augue ipsum, non bibendum arcu pellentesque nec. Praesent rutrum vulputate vehicula. Etiam gravida, risus vel viverra elementum, lorem justo viverra arcu, at tristique eros odio in nisi.")
        public long maxRenderTime = 49827301783444L;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Client client = (Client) o;
            return renderLikeThis == client.renderLikeThis && maxRenderTime == client.maxRenderTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(renderLikeThis, maxRenderTime);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleConfig that = (ExampleConfig) o;
        return version == that.version && character == that.character && Double.compare(that.double$Val, double$Val) == 0 && Objects.equals(generatedTime, that.generatedTime) && Objects.equals(testStr, that.testStr) && Objects.equals(flags, that.flags) && Objects.equals(client, that.client) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, generatedTime, character, double$Val, testStr, flags, client, users);
    }

    public enum Mode {
        ALPHA,
        BETA,
        GAMMA,
        DELTA
    }
}
