package nl.degrijs.security;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import lombok.Builder;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import nl.degrijs.httpserver.http.HttpServerLoom;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class Certificate {
    private static final String              SUN_X509 = "SunX509";
    private              String              keyStoreFilename;
    private              String              storePassword;
    private              String              keyPassword;
    private              String              alias;
    private              String              protocol;
    private              KeyManagerFactory   keyManagerFactory;
    private              TrustManagerFactory trustManagerFactory;
    private              File                keyStoreFile;
    private              SSLContext          sslContext;

    public static Certificate create() {
        return new Certificate();
    }


    /**
     * Call this method to initialize HttpsServer. It will use the configuration file to setup the security.
     * @param server
     * @throws Exception
     */
    public void init(HttpsServer server) throws Exception {
        validateCertificate();
        @Cleanup FileInputStream fis      = new FileInputStream(keyStoreFile);
        KeyStore                 keySTore = KeyStore.getInstance("JSK");
        keySTore.load(fis, storePassword.toCharArray());
        java.security.cert.Certificate certificate = keySTore.getCertificate(alias);
        keyManagerFactory = KeyManagerFactory.getInstance(SUN_X509);
        trustManagerFactory = TrustManagerFactory.getInstance(SUN_X509);
        keyManagerFactory.init(keySTore, keyPassword.toCharArray());
        trustManagerFactory.init(keySTore);
        sslContext = SSLContext.getInstance(protocol);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    SSLContext context = SSLContext.getDefault();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSslParameters = context.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSslParameters);
                }
                catch (Exception e) {
                    throw new IllegalStateException("Failed to setup server", e);
                }
            }
        });
    }

    private void validateCertificate() {
        if (protocol == null || protocol.isBlank()) {
            throw new IllegalStateException("Protocol is missing");
        }
        if (keyStoreFilename == null) {
            throw new IllegalStateException("Key store file is missing");
        }
        keyStoreFile = new File(keyStoreFilename);
        if (!keyStoreFile.exists()) {
            throw new IllegalStateException("Key store file does not exist");
        }
        if (storePassword == null || storePassword.isBlank()) {
            throw new IllegalStateException("Store password is missing");
        }
        if (keyPassword == null || keyPassword.isBlank()) {
            throw new IllegalStateException("Key password is missing");
        }
        if (alias == null || alias.isBlank()) {
            throw new IllegalStateException("Alias is missing");
        }
    }


    public Certificate keyPassword(String property) {
        keyPassword = property;
        return this;
    }

    public Certificate storePassword(String property) {
        storePassword = property;
        return this;
    }

    public Certificate keyStoreFilename(String property) {
        keyStoreFilename = property;
        return this;
    }

    public Certificate alias(String property) {
        alias = property;
        return this;
    }

    public Certificate build() {
        return this;
    }
}
