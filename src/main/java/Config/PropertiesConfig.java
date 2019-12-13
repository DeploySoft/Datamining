package Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {

    private final String rootPath;
    private Properties properties;

    public PropertiesConfig() {
        rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = getClass().getClassLoader().getResource("app.properties").getPath();
        try {
            properties = new Properties();
            properties.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.err.println("properties file not found");
            System.exit(1);
        }

    }

    public String getRootPath() {
        return rootPath;
    }

    public Properties getProperties() {
        return properties;
    }
}
