package nl.degrijs.router;

import com.sun.net.httpserver.HttpExchange;
import nl.degrijs.security.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Rudolf de Grijs
 */
public class WebRouting {
    List<RouteEntry> instances;
    private Roles roles;

    public WebRouting(Roles roles) {
        this.instances = new ArrayList<>();
        this.roles = roles;
    }

    public WebRouting addRoute(MethodAction action, String path, String role, ProcessRequest processor) {
        if (!roles.exists(role)) {
            throw new RouterException("Role " + role + " is unknown");
        }

        RouteEntry entry = createInstance(action, path, role, processor);
        instances.add(entry);
        return this;
    }

    private RouteEntry createInstance(MethodAction action, String path, String role, ProcessRequest processor) {
        if (path.indexOf('(') > -1) {
            return new RoutePatternKey(action, path, role, processor);
        } else {
            return new RouteStringKey(action, path, role, processor);
        }
    }

    public Optional<RouteEntry> matchEntry(HttpExchange request) {
        Optional<RouteEntry> routeEntry = instances.stream()
                .filter(entry -> entry.matches(request))
                .findFirst();
        return routeEntry;
    }


}
