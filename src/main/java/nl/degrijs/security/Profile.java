package nl.degrijs.security;

public class Profile {
    private String  email;
    private String  username;
    private String  role;
    private boolean picture;

    public static Profile ANONYMOUS_PROFILE = new Profile("anonymous@anonymous.unknown", "anonymous", "anonymous", false);

    public Profile(String email, String username, String role, boolean picture) {
        super();
        this.email = email;
        this.username = username;
        this.role = role;
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isPicture() {
        return picture;
    }

    public boolean isAnonymous() {
        return this == ANONYMOUS_PROFILE;
    }

}
