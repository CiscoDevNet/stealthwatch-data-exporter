//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import java.util.ArrayList;
import java.util.Collection;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;

import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_FACTORY_MANAGER_ALGORITHM;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_FILE;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_PASSWORD;
import static org.glassfish.tyrus.client.SslContextConfigurator.KEY_STORE_TYPE;
import static org.glassfish.tyrus.client.SslContextConfigurator.TRUST_STORE_FILE;
import static org.glassfish.tyrus.client.SslContextConfigurator.TRUST_STORE_PASSWORD;
import static org.glassfish.tyrus.client.SslContextConfigurator.TRUST_STORE_TYPE;

class FlowForwarderClient {

    private final Collection<FlowCollector> flowCollectors = new ArrayList<>();

    FlowForwarderClient(Configuration configuration) {
        if (configuration.debugSSlConnection) {
            System.setProperty("javax.net.debug", "all");
        }
        System.setProperty(KEY_FACTORY_MANAGER_ALGORITHM, "SunX509");
        System.setProperty(KEY_STORE_FILE, configuration.keyStorePath);
        System.setProperty(KEY_STORE_PASSWORD, configuration.keyStorePassword);
        System.setProperty(KEY_STORE_TYPE, "PKCS12");
        System.setProperty(TRUST_STORE_FILE, configuration.trustStorePath);
        System.setProperty(TRUST_STORE_PASSWORD, configuration.trustStorePassword);
        System.setProperty(TRUST_STORE_TYPE, "PKCS12");

        SslContextConfigurator defaultConfig = new SslContextConfigurator();
        defaultConfig.setKeyStorePassword(configuration.keyStorePassword);
        defaultConfig.setTrustStorePassword(configuration.trustStorePassword);

        final SslEngineConfigurator sslEngineConfigurator =
                new SslEngineConfigurator(defaultConfig, true, false, false);
        sslEngineConfigurator.setHostVerificationEnabled(!configuration.bypassHostVerification);

        configuration.hosts.stream()
                           .map(host -> host.startsWith("wss")
                                   ? new FlowCollector(host, sslEngineConfigurator)
                                   : new FlowCollector(host))
                           .forEach(flowCollectors::add);
    }

    /**
     * Starts flow collectors.
     * Each flow collector will be started in separate thread.
     */
    void forwardFlows() {
        flowCollectors.forEach(FlowCollector::startSession);
    }

    /**
     * Stops flow collectors.
     */
    void stop() {
        flowCollectors.forEach(FlowCollector::closeSession);
    }
}
