package red.jackf.tomlconfig.data;

import red.jackf.tomlconfig.parser.token.BareStringToken;
import red.jackf.tomlconfig.parser.token.StringToken;
import red.jackf.tomlconfig.parser.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TOMLKey {
    private final List<String> path;

    public TOMLKey(String path) {
        this(Collections.singletonList(path));
    }

    public TOMLKey(List<String> path) {
        this.path = path;
    }

    public List<String> getPath() {
        return path;
    }

    public static TOMLKey of(List<Token> tokens) {
        List<String> path = new ArrayList<>(tokens.size());
        for (Token token : tokens) {
            if (token instanceof BareStringToken) {
                path.add(((BareStringToken) token).getContents());
            } else if (token instanceof StringToken) {
                path.add(((StringToken) token).getText());
            } else {
                throw new IllegalArgumentException("Non-String token passed to TOMLKey.of()");
            }
        }
        return new TOMLKey(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TOMLKey tomlKey = (TOMLKey) o;
        return Objects.equals(path, tomlKey.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "Key<" + String.join(".", path) + '>';
    }
}
