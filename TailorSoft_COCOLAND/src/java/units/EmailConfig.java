package units;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Đọc thông tin email từ file resources/email.properties.
 */
public class EmailConfig {

    private static final Properties props = new Properties();

    static {
        // Try loading configuration from the bundled resources first
        loadFromClasspath("conf/email.properties");
        // Some IDEs place resources directly in the classpath root
        if (props.isEmpty()) {
            loadFromClasspath("email.properties");
        }

        // Fallback to environment variables so the application can run
        // even when the properties file is missing.  This is useful in
        // containerised deployments where secrets are provided via env vars.
        if (props.isEmpty()) {
            String envMail = System.getenv("EMAIL_USER");
            String envPwd  = System.getenv("EMAIL_PASSWORD");
            if (envMail != null) props.setProperty("email", envMail);
            if (envPwd  != null) props.setProperty("password", envPwd);
            if (props.isEmpty()) {
                System.err.println("email.properties not found and no environment variables provided");
            }
        }
    }

    private static void loadFromClasspath(String path) {
        try (InputStream in = EmailConfig.class.getClassLoader().getResourceAsStream(path)) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println(path + " not found");
            }
        } catch (IOException e) {
            System.err.println("Failed to load " + path + ": " + e.getMessage());
        }
    }

    public static boolean isConfigured() {
        return !getEmail().isBlank() && !getPassword().isBlank();
    }

    public static String getEmail() {
        return props.getProperty("email", "");
    }

    public static String getPassword() {
        // Gmail application passwords are often written with spaces for
        // readability.  Remove them to avoid authentication errors.
        return props.getProperty("password", "").replace(" ", "");
    }
    
    
    
}
