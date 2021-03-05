package nl.degrijs;

import lombok.Getter;
import nl.degrijs.security.Certificate;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

@Getter
public class Configuration {
    private String      host;
    private int         port;
    private String      context;
    private Path        logdir;
    private boolean     secure;
    private int         backlog;
    private Certificate certificate;

    public Configuration() throws Exception {
        Properties properties = new Properties();
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("httpserver.properties")) {
            properties.load(is);
            host = properties.getProperty("http.host", "localhost");
            port = Integer.parseInt(properties.getProperty("http.port", "8080"));
            context = properties.getProperty("http.context", "/application");
            logdir = new File(properties.getProperty("http.loh", "/log")).toPath();
            secure = Boolean.parseBoolean(properties.getProperty("http.secure", "false"));
            backlog = Integer.parseInt(properties.getProperty("http.backlog", "0"));
            if (secure) {
                certificate = createCertificate(properties);
            }
        }
    }

    private Certificate createCertificate(Properties properties) {
//        http.secure.keystore=c:/temp/mycert.keystore
//        http.secure.store.password=mycert
//        http.secure.key.password=mycert
//        http.secure.alias=alias

        Certificate certificate =
                Certificate.create()
                        .keyPassword(properties.getProperty("http.secure.store.password"))
                        .storePassword(properties.getProperty("http.secure.store.password"))
                        .keyStoreFilename(properties.getProperty("http.secure.keystore"))
                        .alias(properties.getProperty("http.secure.alias"))
                        .build();


        return certificate;
    }

}
