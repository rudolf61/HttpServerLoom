package nl.degrijs.router;

import nl.degrijs.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rudolf de Grijs
 */
public class RoutePatternKey extends AbstractRouteEntry {

    private static final Pattern parameters = Pattern.compile("\\(([a-z0-9_-]+)\\:(int|string)\\)");

    private final Pattern      pattern;
    private final List<String> keys;

    public RoutePatternKey(MethodAction action, String path, String role, ProcessRequest processor) {
        super(action, role, processor);
        String normPath = normalize(path);

        Matcher      m  = parameters.matcher(normPath);
        StringBuffer sb = new StringBuffer();
        keys = new ArrayList<>();

        while (m.find()) {
            keys.add(m.group(1));
            String replaceValue = getRegex(m.group(2));
            m.appendReplacement(sb, replaceValue);
        }

        m.appendTail(sb);


        pattern = Pattern.compile(sb.toString());
    }

    private String getRegex(String type) {
        String regex = "[^/]+";
        switch (type) {
            case "int":
                regex = "\\d+";
                break;
            default:
                break;
        }

        return Matcher.quoteReplacement("(" + regex + ")");
    }


    @Override
    protected Pair<Boolean, Map<String, String>> processRequest(MethodAction action, String path) {

        String  normPath = normalize(path);
        Matcher matcher  = pattern.matcher(normPath);

        if (matcher.matches()) {

            int                     count       = matcher.groupCount();
            HashMap<String, String> matchValues = new HashMap<>();

            for (int i = 1; i <= count; i++) {
                String value = matcher.group(i);
                String key   = keys.get(i - 1);
                matchValues.put(key, value);
            }

            return Pair.of(Boolean.TRUE, matchValues);
        } else {
            return Pair.of(Boolean.FALSE, Collections.EMPTY_MAP);
        }

    }

}
