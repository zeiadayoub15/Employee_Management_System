package Backend;

/** Demonstration-only login credentials specified by the lab handout. */
public final class AuthService
{
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private AuthService() { }

    public static boolean authenticate(String username, char[] password)
    {
        if (username == null || password == null)
            return false;
        return USERNAME.equals(username.trim()) && PASSWORD.equals(new String(password));
    }
}
