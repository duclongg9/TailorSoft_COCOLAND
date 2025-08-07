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
        try (InputStream in = EmailConfig.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (in != null) props.load(in);
            else System.err.println("⚠️  email.properties not found");
        } catch (IOException e) {
            System.err.println("❌  Failed to load email.properties: " + e.getMessage());
        }
    }

    public static boolean isConfigured() {
        return !getEmail().isBlank() && !getPassword().isBlank();
    }

    public static String getEmail() {
        return props.getProperty("email", "");
    }

    public static String getPassword() {
        return props.getProperty("password", "");
    }
}