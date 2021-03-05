package nl.degrijs.router;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

public class RouterUtil {
	public static final String PATH_PARAMS = "__pathParams";

	public static final String getPathParam(HttpExchange request, String name) {
		Map<String, String> pathParams = (Map<String, String>) request.getAttribute(PATH_PARAMS);

		if (pathParams != null && name != null && pathParams.containsKey(name)) {
			return pathParams.get(name);
		}
		
		return null;
	}


}
