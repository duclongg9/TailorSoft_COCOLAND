package units;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailConfig {

    private static final Properties props = new Properties();

    static {
        try {
            // Đường dẫn tới file trong thư mục conf (cùng cấp với thư mục java)
            FileInputStream input = new FileInputStream("src/conf/email.properties");
            props.load(input);
        } catch (IOException e) {
            System.err.println("❌ Không đọc được file email.properties: " + e.getMessage());
        }
    }

    public static String getEmail() {
        return props.getProperty("email");
    }

    public static String getPassword() {
        return props.getProperty("password");
    }

}
