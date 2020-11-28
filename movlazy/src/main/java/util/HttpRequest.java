package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 */
public class HttpRequest implements IRequest {

    @Override
    public InputStream getBody(String path) {
        try {
            return new URL(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}