package nl.degrijs.router;

import com.sun.net.httpserver.HttpExchange;

@FunctionalInterface
public interface ProcessRequest {
    void process(HttpExchange exchange) throws RouterException;
}
