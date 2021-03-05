package nl.degrijs.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Roles implements Iterable<String> {
    public static final String       ANONYMOUS = "anonymous";
    private             List<String> roles;
    private static      Roles        ROLES;

    public static Roles createRoles(String... role) {
        ROLES = new Roles(role);
        return ROLES;
    }

    public static Roles getInstance() {
        return ROLES;
    }


    /**
     * Order is important n has all the rights of n-1. So you might e.g. start with anonymous
     *
     * @param role
     */
    public Roles(String... role) {
        roles = new ArrayList<>();
        for (String r : roles) {
            roles.add(r.toLowerCase());
        }
    }

    public boolean exists(String role) {
        return roles.indexOf(role.toLowerCase()) > -1;
    }

    @Override
    public Iterator<String> iterator() {
        return roles.iterator();
    }

    public boolean allowed(String userRole, String minimalRole) {
        return roles.indexOf(userRole.toLowerCase()) >= roles.indexOf(minimalRole.toLowerCase());
    }


}
