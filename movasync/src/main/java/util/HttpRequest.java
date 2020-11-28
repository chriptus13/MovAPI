package util;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;

import static util.Logging.log;

/**
 *
 */
public class HttpRequest implements IRequest {
    private final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<String> getBody(String path) {
        log("getbody for {0} ", path);
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient.prepareGet(path);
        return boundRequestBuilder.execute().toCompletableFuture()
                .thenApplyAsync(response -> { // applyAsync devido a um deadlock na Thread do AsyncHttpClient (Luís Falcão 2018-06-15)
                    log("Response for {0} ", path);
                    return response;
                }).thenApply(Response::getResponseBody);
    }
}