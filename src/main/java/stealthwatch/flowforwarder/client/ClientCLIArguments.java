package stealthwatch.flowforwarder.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class handling ClientCLIArguments.
 */
public class ClientCLIArguments {

    /**
     * The asking for help flag.
     */
    private boolean askingForHelp;

    /**
     * The key store path default value.
     */
    private String keyStorePath = "ssl/client.p12";

    /**
     * The key store password default value.
     */
    private String keyStorePassword = "pw.client-export";

    /**
     * The trust store path default value.
     */
    private String trustStorePath = "ssl/truststore.p12";

    /**
     * The trust store password default value.
     */
    private String trustStorePassword = "pw.trust";

    /**
     * The bypass host verification.
     */
    private boolean bypassHostVerification;

    /**
     * Debug ssl connection.
     */
    private boolean debugSSlConnection;

    /**
     * Secure connection is needed.
     */
    private boolean needSecureConnection;

    /**
     * The hosts.
     */
    private List<String> hosts;

    /**
     * Checks if is asking for help.
     *
     * @return true, if is asking for help
     */
    public boolean isAskingForHelp() {
        return askingForHelp;
    }

    /**
     * Sets the asking for help.
     *
     * @param askingForHelp the new asking for help
     */
    public void setAskingForHelp(boolean askingForHelp) {
        this.askingForHelp = askingForHelp;
    }

    /**
     * Gets the key store path.
     *
     * @return the key store path
     */
    public String getKeyStorePath() {
        return keyStorePath;
    }

    /**
     * Sets the key store path.
     *
     * @param keyStorePath the new key store path
     */
    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    /**
     * Gets the key store password.
     *
     * @return the key store password
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * Sets the key store password.
     *
     * @param keyStorePassword the new key store password
     */
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    /**
     * Gets the trust store path.
     *
     * @return the trust store path
     */
    public String getTrustStorePath() {
        return trustStorePath;
    }

    /**
     * Sets the trust store path.
     *
     * @param trustStorePath the new trust store path
     */
    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    /**
     * Gets the trust store password.
     *
     * @return the trust store password
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * Sets the trust store password.
     *
     * @param trustStorePassword the new trust store password
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * Checks if is bypass host verification.
     *
     * @return true, if is bypass host verification
     */
    public boolean isBypassHostVerification() {
        return bypassHostVerification;
    }

    /**
     * Sets the bypass host verification.
     *
     * @param bypassHostVerification the new bypass host verification
     */
    public void setBypassHostVerification(boolean bypassHostVerification) {
        this.bypassHostVerification = bypassHostVerification;
    }

    /**
     * Checks if is debug ssl connection.
     *
     * @return true, if is debug ssl connection
     */
    public boolean isDebugSSlConnection() {
        return debugSSlConnection;
    }

    /**
     * Sets the debug SSL connection.
     *
     * @param debugSSlConnection the new debug SSl connection
     */
    public void setDebugSSlConnection(boolean debugSSlConnection) {
        this.debugSSlConnection = debugSSlConnection;
    }

    /**
     * Adds the host.
     *
     * @param host the host
     */
    public void addHost(String host) {
        if (hosts == null) {
            hosts = new ArrayList<>(1);
        }
        hosts.add(host);

        if(host.startsWith("wss")) {
            needSecureConnection = true;
        }
    }

    /**
     * Gets the hosts.
     *
     * @return the hosts
     */
    public List<String> getHosts() {
        return hosts;
    }

    /**
     * @return true if secure connection is needed
     */
    public boolean isNeedSecureConnection() {
        return needSecureConnection;
    }

    /**
     * Checks if arguments are valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        // validating help and servers
        boolean result = !askingForHelp && (hosts != null && !hosts.isEmpty());
        if (result) {
            // if secure connection is needed validate secure properties
            if (needSecureConnection) {
                result = validSecureProp();
            }
        }
        return result;
    }

    /**
     * Validate secure properties.
     *
     * @return true, if properties are valid
     */
    private boolean validSecureProp() {
        if (validString(keyStorePath) && validString(trustStorePath)) {
            File ks = new File(keyStorePath);
            File ts = new File(trustStorePath);
            return ks.exists() && ks.canRead() && ts.exists() && ts.canRead();
        }

        return false;
    }

    /**
     * Valid string.
     *
     * @param val the val
     * @return true, if successful
     */
    private boolean validString(String val) {
        return val != null && !val.isEmpty();
    }

}
