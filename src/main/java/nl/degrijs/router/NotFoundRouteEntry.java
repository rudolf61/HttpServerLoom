package nl.degrijs.router;

import nl.degrijs.util.Pair;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class NotFoundRouteEntry extends AbstractRouteEntry {

    private static final Pair<Boolean, Map<String, String>> SINGLE_RESPONSE = Pair.of(Boolean.TRUE, Collections.EMPTY_MAP);

    public NotFoundRouteEntry() {
        super(MethodAction.GET, "anonymous", (exchange) -> {
            try {
                exchange.sendResponseHeaders(404, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected Pair<Boolean, Map<String, String>> processRequest(MethodAction action, String path) {
        return SINGLE_RESPONSE;
    }

}
