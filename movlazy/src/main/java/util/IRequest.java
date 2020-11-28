package util;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IRequest {
    InputStream getBody(String path);

    default IRequest compose(Consumer<String> cons) {
        return path -> {
            cons.accept(path);
            return getBody(path);
        };
    }
}
