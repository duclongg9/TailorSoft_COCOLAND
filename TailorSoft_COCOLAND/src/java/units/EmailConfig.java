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

    public static String getEmail() {
        String envEmail = System.getenv("GMAIL_USER");
        if (envEmail != null && !envEmail.isBlank()) {
            return envEmail;
        }
        return props.getProperty("email");
    }

    public static String getPassword() {
        String envPass = System.getenv("GMAIL_PASS");
        if (envPass != null && !envPass.isBlank()) {
            return envPass;
        }
        return props.getProperty("password");
    }

}
