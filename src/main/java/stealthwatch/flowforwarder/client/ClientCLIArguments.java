//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * The Class handling ClientCLIArguments.
 */
class ClientCLIArguments {

    private boolean askingForHelp;
    private boolean bypassHostVerification;
    private boolean debugSSlConnection;

    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;

    private final List<String> hosts = new ArrayList<>();

    boolean isAskingForHelp() {
        return askingForHelp;
    }

    void setAskingForHelp(boolean askingForHelp) {
        this.askingForHelp = askingForHelp;
    }

    boolean isBypassHostVerification() {
        return bypassHostVerification;
    }

    void setBypassHostVerification(boolean bypassHostVerification) {
        this.bypassHostVerification = bypassHostVerification;
    }

    boolean isDebugSSlConnection() {
        return debugSSlConnection;
    }

    void setDebugSSlConnection(boolean debugSSlConnection) {
        this.debugSSlConnection = debugSSlConnection;
    }

    String getKeyStorePath() {
        return keyStorePath;
    }

    void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    String getKeyStorePassword() {
        return keyStorePassword;
    }

    void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    String getTrustStorePath() {
        return trustStorePath;
    }

    void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    String getTrustStorePassword() {
        return trustStorePassword;
    }

    void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    List<String> getHosts() {
        return unmodifiableList(hosts);
    }

    void addHost(String host) {
        hosts.add(host);
    }

    boolean isValid() {
        if (askingForHelp || hosts.isEmpty()) {
            return false;
        }
        if (!anySecureConnections()) {
            return true;
        }
        return validSecureConnectionProperties();
    }

    boolean anySecureConnections() {
        return hosts.stream().anyMatch(h -> h.startsWith("wss:"));
    }

    private boolean validSecureConnectionProperties() {
        return validPath(keyStorePath) && validString(keyStorePassword) &&
               validPath(trustStorePath) && validString(trustStorePassword);
    }

    private static boolean validPath(String path) {
        return validString(path) && new File(path).canRead();
    }

    private static boolean validString(String val) {
        return val != null && !val.isEmpty();
    }
}
