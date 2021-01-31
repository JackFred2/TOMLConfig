package red.jackf.tomlconfig;

import red.jackf.tomlconfig.annotations.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestConfig implements Config {
    public ClientOptions client = new ClientOptions();
    public ServerOptions server = new ServerOptions();

    @Transitive
    public static class ClientOptions {
        public boolean renderQuickly = false;
        public int actionKeyBinding = 293;
    }

    @Transitive
    public static class ServerOptions {
        public String welcomeMessage = "Welcome to the server";
        public List<String> adminNames = new ArrayList<>();
        {
            adminNames.add("Jack");
            adminNames.add("Fred");
        }
        public Set<User> allowedUsers = new HashSet<>();
        {
            allowedUsers.add(new User("Jack", 1));
            allowedUsers.add(new User("Fred", 2));
            allowedUsers.add(new User("Alice", 3));
            allowedUsers.add(new User("Bob", 7));
            allowedUsers.add(new User("Charlie", 11));
        }
    }

    @Transitive
    public static class User {
        public String name;
        public int id;

        private User() {}

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }
}
