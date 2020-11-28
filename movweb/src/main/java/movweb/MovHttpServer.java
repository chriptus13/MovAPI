package movweb;

import com.google.common.util.concurrent.RateLimiter;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import movasync.MovService;
import movasync.MovWebApi;
import movasync.model.Credit;
import movweb.utils.Utils;
import util.HttpRequest;

import java.util.Comparator;

import static java.util.stream.Collectors.toList;
import static util.Logging.log;

public class MovHttpServer {
    private static final int PORT = 8080;

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(3.0);
    private static final MovService MOV_SERVICE = new MovService(new MovWebApi(new HttpRequest().compose(__ -> RATE_LIMITER.acquire())));

    private static final String TEMPLATES_DIR = "templates";
    private static final HandlebarsTemplateEngine handlebarsTemplateEngine = HandlebarsTemplateEngine.create();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        final HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        registerRoutes(router);
        httpServer.requestHandler(router::accept);

        httpServer.listen(PORT);
    }

    private static void registerRoutes(Router router) {
        //router.route().handler(FaviconHandler.create("favicon.ico"));
        router.route("/").handler(MovHttpServer::index);
        router.route("/movies").handler(MovHttpServer::getMovies);
        router.route("/movies/:id").handler(MovHttpServer::getMovie);
        router.route("/movies/:id/credits").handler(MovHttpServer::getMovieCredits);
        router.route("/person/:id").handler(MovHttpServer::getPerson);
        router.route("/person/:id/movies").handler(MovHttpServer::getPersonMovies);
    }

    private static void index(RoutingContext ctx) {
        renderTemplate(ctx, "/index");
    }

    private static void getMovies(RoutingContext ctx) {
        ctx.response().headers().add("Content-Type", "Content-Type: text/html; charset=utf-8");
        String name = ctx.request().getParam("name");
        if(name == null || name.isEmpty()) renderError(ctx, "Query must be provided");
        else MOV_SERVICE.search(name)
                .thenAccept(searchItemStream -> {
                    ctx.put("title", "Movies");
                    ctx.put("header", "Search Results");
                    ctx.put("movies", searchItemStream.collect(toList()));
                    renderTemplate(ctx, "/movies");
                });
    }

    private static void getMovie(RoutingContext ctx) {
        ctx.response().headers().add("Content-Type", "Content-Type: text/html; charset=utf-8");
        String id = ctx.request().getParam("id");
        if(!Utils.isUnsignedInt(id)) renderError(ctx, "Invalid ID");
        else MOV_SERVICE.getMovie(Integer.valueOf(id))
                .thenAccept(movie -> {
                    ctx.put("movie", movie);
                    renderTemplate(ctx, "/movie");
                });
    }

    private static void getMovieCredits(RoutingContext ctx) {
        ctx.response().headers().add("Content-Type", "Content-Type: text/html; charset=utf-8");
        String id = ctx.request().getParam("id");
        if(!Utils.isUnsignedInt(id)) renderError(ctx, "Invalid ID");
        else MOV_SERVICE.getMovieCast(Integer.valueOf(id))
                .thenAccept(creditStream -> {
                    ctx.put("title", "Credits");
                    ctx.put("header", "Credits");
                    ctx.put("credits", creditStream.sorted(Comparator.comparing(Credit::getName)).collect(toList()));
                    renderTemplate(ctx, "/movieCredits");
                });
    }

    private static void getPerson(RoutingContext ctx) {
        ctx.response().headers().add("Content-Type", "Content-Type: text/html; charset=utf-8");
        String id = ctx.request().getParam("id");
        if(!Utils.isUnsignedInt(id)) renderError(ctx, "Invalid ID");
        else MOV_SERVICE.getActor(Integer.valueOf(id))
                .thenAccept(person -> {
                    ctx.put("person", person);
                    renderTemplate(ctx, "/person");
                });
    }

    private static void getPersonMovies(RoutingContext ctx) {
        ctx.response().headers().add("Content-Type", "Content-Type: text/html; charset=utf-8");
        String id = ctx.request().getParam("id");
        if(!Utils.isUnsignedInt(id)) renderError(ctx, "Invalid ID");
        else MOV_SERVICE.getActorCreditsCast(Integer.valueOf(id))
                .thenAccept(searchItemStream -> {
                    ctx.put("title", "Movies");
                    ctx.put("header", "Movies");
                    ctx.put("movies", searchItemStream.collect(toList()));
                    renderTemplate(ctx, "/personMovies");
                });
    }

    private static void renderError(RoutingContext ctx, String error) {
        ctx.put("error", error);
        renderTemplate(ctx, "/error");
    }

    private static void renderTemplate(RoutingContext ctx, String templateName) {
        log("render template {0}", templateName);
        handlebarsTemplateEngine.render(ctx, TEMPLATES_DIR, templateName, res -> sendTemplateResult(ctx, res));
    }


    private static void sendTemplateResult(RoutingContext ctx, AsyncResult<Buffer> res) {
        if(res.succeeded()) ctx.response().end(res.result());
        else {
            log("Error executing template: ", res.cause());
            ctx.fail(res.cause());
        }
    }
}
