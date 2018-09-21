package stealthwatch.flowforwarder.client;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig.Configurator;

import static java.util.Collections.singletonList;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_FILE;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_PASSWORD;

class EndpointConfigurator extends Configurator {

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("X-nginx-ssl-client-cert", singletonList(base64EncodedCertificate()));
    }

    private static String base64EncodedCertificate() {
        try {
            KeyStore keystore = KeyStore.getInstance("pkcs12");
            keystore.load(new FileInputStream(System.getProperty(KEY_STORE_FILE)),
                          System.getProperty(KEY_STORE_PASSWORD).toCharArray());
            String alias = keystore.aliases().nextElement();
            return Base64.getEncoder().encodeToString(keystore.getCertificate(alias).getEncoded());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error retrieving key store certificate from " + System.getProperty(KEY_STORE_FILE), e);
        }
    }
}
