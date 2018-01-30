//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import org.glassfish.tyrus.client.SslEngineConfigurator;

/**
 * This class handles running and stopping collector-session thread.
 */
class FlowCollector {
    private final String                hostAddress;
    private       FlowCollectorSession  session;
    private       SslEngineConfigurator sslEngineConfigurator;

    FlowCollector(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    FlowCollector(String hostAddress, SslEngineConfigurator sslEngineConfigurator) {
        this.hostAddress = hostAddress;
        this.sslEngineConfigurator = sslEngineConfigurator;
    }

    void startSession() {
        session = new FlowCollectorSession(hostAddress, sslEngineConfigurator);
        session.start();
    }

    void closeSession() {
        session.interrupt();
    }
}
