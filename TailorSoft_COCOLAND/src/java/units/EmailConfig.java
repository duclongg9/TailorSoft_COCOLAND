package units;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = EmailConfig.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("⚠️ Không tìm thấy file email.properties trong classpath.");
            }
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi đọc email.properties: " + e.getMessage());
        }
    }

    public static boolean isConfigured() {
        String email = props.getProperty("email");
        String password = props.getProperty("password");
        return email != null && !email.isBlank() && password != null && !password.isBlank();
    }

    public static String getEmail() {
        String email = props.getProperty("email");
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Missing 'email' in email.properties");
        }
        return email;
    }

    public static String getPassword() {
        String password = props.getProperty("password");
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Missing 'password' in email.properties");
        }
        return password;
    }

}
