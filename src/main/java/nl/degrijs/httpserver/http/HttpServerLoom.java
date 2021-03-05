package nl.degrijs.httpserver.http;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import nl.degrijs.Configuration;
import nl.degrijs.exception.ExceptionCategory;
import nl.degrijs.exception.HttpException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * Http server SUN using virtual threads to dispatch requests
 */
public class HttpServerLoom {

    HttpServer    httpServer;
    Configuration configuration;

    public static class Builder {
        HttpServerLoom server;

        public Builder() throws HttpException {
            server = new HttpServerLoom();
            server.init();
        }

        public HttpServerLoom build() {
            return server;
        }
    }


    private HttpServerLoom() throws HttpException {
        try {
            configuration = new Configuration();
        } catch (Exception e) {
            throw new HttpException(ExceptionCategory.Configuration, e);
        }
    }

    void init() throws HttpException {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(configuration.getHost()), configuration.getPort());
            if (configuration.isSecure()) {
                HttpsServer httpsServer = HttpsServer.create(socketAddress, configuration.getBacklog());
                configuration.getCertificate()
                        .init(httpsServer);

            } else {
                httpServer = HttpServer.create(socketAddress, configuration.getBacklog());
            }
        } catch (Exception e) {
            throw new HttpException(ExceptionCategory.Configuration, e);
        }

        var executor = Executors.newVirtualThreadExecutor();
        httpServer.setExecutor(executor);
    }

    public HttpLoomContext createContext(String context) {
        var ctx = normalizeContext(context);

        return new HttpLoomContext(ctx, this, httpServer.createContext(ctx));
    }


    public HttpLoomContext createContext(String context, HttpHandler handler) {
        var ctx = normalizeContext(context);
        if (handler == null) {
            throw new IllegalArgumentException("Handler missing");
        }

        return new HttpLoomContext(ctx, this, httpServer.createContext(ctx, handler));
    }

    private String normalizeContext(String context) {
        if (context == null || context.isBlank()) {
            throw new IllegalArgumentException("Context missing");
        }

        if (!context.startsWith("/")) {
            context = "/" + context;
        }
        if (context.endsWith("/")) {
            context.substring(0, context.length() - 1);
        }
        return context;
    }

    public void start() {
        httpServer.start();
    }

    public void stop(Duration duration) {
        httpServer.stop(duration.toSecondsPart());
    }

}
