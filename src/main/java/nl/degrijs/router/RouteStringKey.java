package nl.degrijs.router;

import nl.degrijs.util.Pair;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Rudolf de Grijs
 */
public class RouteStringKey extends AbstractRouteEntry {
    private String route;

    public RouteStringKey(MethodAction action, String route, String role, ProcessRequest request) {
        super(action, role, request);
        this.route = normalize(route);
    }

    @Override
    protected Pair<Boolean, Map<String, String>> processRequest(MethodAction method, String path) {
        Boolean matches = Boolean.valueOf(method == getMethod() && normalize(path).equalsIgnoreCase(route));
        return Pair.of(matches, Collections.EMPTY_MAP);
    }

}
