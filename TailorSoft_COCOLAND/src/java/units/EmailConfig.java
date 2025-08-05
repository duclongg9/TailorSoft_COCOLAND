package units;

import java.io.InputStream;
import java.util.Properties;

public class EmailConfig {
    private static final Properties props = new Properties();

    private static final String ENV_EMAIL = System.getenv("GMAIL_USER");
    private static final String ENV_PASSWORD = System.getenv("GMAIL_PASS");

    static {
        try (InputStream input = EmailConfig.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEmail() {
        return ENV_EMAIL != null ? ENV_EMAIL : props.getProperty("email");
    }

    public static String getPassword() {
        return ENV_PASSWORD != null ? ENV_PASSWORD : props.getProperty("password");
    }
}
