package nl.degrijs.httpserver.http;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;

import java.util.Set;

public class HttpLoomContext {
    public static final String         PATH = "__PATH__";
    private final       HttpContext    context;
    private final       String         contextPath;
    private final       HttpServerLoom httpServerLoom;

    HttpLoomContext(String path, HttpServerLoom serverLoom, HttpContext httpContext) {
        this.httpServerLoom = serverLoom;
        this.context = httpContext;
        contextPath = path;
        this.context.getAttributes()
                .put(PATH, path);
    }

    public HttpServerLoom getHttpServerLoom() {
        return httpServerLoom;
    }

    public void setHandler(HttpHandler handler) {
        context.setHandler(handler);
    }

    public void addAuthenticator(Authenticator authenticator) {
        context.setAuthenticator(authenticator);
    }

    public void addFilter(Filter filter) {
        context.getFilters()
                .add(filter);
    }

    public <T> HttpLoomContext addAtribute(String key, T value) {
        context.getAttributes()
                .put(key, value);
        return this;
    }

    public <T> T getAttribute(String key) {
        return (T) context.getAttributes()
                .get(key);
    }

    public <T> T getAttribute(String key, T defaultValue) {
        return (T) (context.getAttributes()
                .containsKey(key) ? getAttribute(key) : defaultValue);
    }

    public Set<String> keys() {
        return context.getAttributes()
                .keySet();
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getPath() {
        return context.getPath();
    }
}
