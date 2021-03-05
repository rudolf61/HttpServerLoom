package nl.http.test;

import nl.degrijs.exception.HttpException;
import nl.degrijs.httpserver.http.HttpLoomContext;
import nl.degrijs.httpserver.http.HttpServerLoom;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpTest {
    public static void main(String[] args) throws HttpException {
        long start = System.currentTimeMillis();
        var server = new HttpServerLoom.Builder()
        .build();
        HttpLoomContext context = server.createContext("application");
        context.setHandler(exchange -> {
                    byte[] data = "Hello World".getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(200, data.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(data);
                        os.flush();
                    }
                });
        server.start();
        System.out.println("Server start in " + (System.currentTimeMillis() - start) + " milliseconds");

    }
}
