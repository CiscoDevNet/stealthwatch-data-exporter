package stealthwatch.flowforwarder.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

class Configuration {

    boolean bypassHostVerification;
    boolean debugSSlConnection;

    String keyStorePath;
    String keyStorePassword;

    String trustStorePath;
    String trustStorePassword;

    final Collection<String> hosts = new ArrayList<>();

    Collection<String> validate() {
        Collection<String> errors = new ArrayList<>();
        if (hosts.isEmpty()) {
            errors.add("missing hosts");
        }

        if (anySecureConnections()) {
            errors.addAll(keyStoreErrors(keyStorePath, keyStorePassword));
            errors.addAll(keyStoreErrors(trustStorePath, trustStorePassword));
        }

        return errors;
    }

    private static Collection<String> keyStoreErrors(String path, String password) {
        try {
            loadKeyStore(path, password);
            return emptyList();
        } catch (Exception ignored) {
            return singletonList("Unable to load: " + path);
        }
    }

    private static void loadKeyStore(String path, String password) throws Exception {
        try (InputStream in = new FileInputStream(path)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(in, password.toCharArray());
        }
    }

    private boolean anySecureConnections() {
        return hosts.stream().anyMatch(h -> h.startsWith("wss:"));
    }
}
