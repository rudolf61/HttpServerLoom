package nl.degrijs.router;


import com.sun.net.httpserver.HttpExchange;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by Rudolf de Grijs
 */
public interface RouteEntry {
    boolean matches(HttpExchange request);

    ProcessRequest getProcessor();

    default MethodAction getMethod() {
        return MethodAction.GET;
    }

    default String normalize(String path) {
        String normPath = null;
        if (path.endsWith("/")) {
            normPath = path.substring(0, path.length() - 1);
        } else {
            normPath = path;
        }

        if (!path.startsWith("/")) {
            normPath = "/" + normPath;
        }

        return normPath;
    }

    default Map<String, String> getMatchedValues() {
        return Collections.emptyMap();
    }

    default String getParameter(String key) {
        return null;
    }


    default boolean hasAccess(Function<String, Boolean> role) {
        // return role.apply(getRole());
        return true;
    }

    default boolean containsKey(String key) {
        return false;
    }

    default boolean isAsynchronous() {
        return false;
    }

    @SuppressWarnings("unchecked")
    default Set<String> keySet() {
        return Collections.EMPTY_SET;
    }

}
