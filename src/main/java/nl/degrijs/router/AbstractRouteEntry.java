package nl.degrijs.router;

import com.sun.net.httpserver.HttpExchange;
import nl.degrijs.security.Profile;
import nl.degrijs.security.Roles;
import nl.degrijs.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rudolf de Grijs
 */
public abstract class AbstractRouteEntry implements RouteEntry {
    private ProcessRequest processor;
    private MethodAction   action;
    private String         role;

    public AbstractRouteEntry(MethodAction action, String role, ProcessRequest processor) {
        this.processor = processor;
        this.action = action;
        this.role = role;
    }

    public void process(HttpExchange exchange) throws RouterException {
        processor.process(exchange);
    }

    @Override
    public ProcessRequest getProcessor() {
        return processor;
    }

    public MethodAction getAction() {
        return action;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean matches(HttpExchange exchange) {
        Profile profile = (Profile) exchange.getAttribute("__profile");

        if (!Roles.getInstance()
                .allowed(profile.getRole(), getRole())) {
            return false;
        }

        var requestUri = exchange.getRequestURI();
        var reqPath    = requestUri.getPath();
        var pathIndex  = reqPath.indexOf('/', 1);

        MethodAction                       action         = MethodAction.valueOf(exchange.getRequestMethod());
        String                             path           = pathIndex == -1 ? "/" : reqPath.substring(pathIndex);
        Map<String, String>                matchingValues = new HashMap<>();
        Pair<Boolean, Map<String, String>> result         = processRequest(action, path);
        if (!result.get_1()) {
            return false;
        } else if (result.get_2()
                .size() > 0) {
            exchange.setAttribute(RouterUtil.PATH_PARAMS, result.get_2());
        }

        return true;

    }

    protected abstract Pair<Boolean, Map<String, String>> processRequest(MethodAction action, String path);

}
