package units;

import java.io.InputStream;
import java.util.Properties;

public class EmailConfig {
    private static final Properties props = new Properties();

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
        return props.getProperty("email");
    }

    public static String getPassword() {
        return props.getProperty("password");
    }
}
