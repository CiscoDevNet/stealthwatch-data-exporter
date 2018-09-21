//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------


package stealthwatch.flowforwarder.client;

import java.io.IOException;
import java.net.URI;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslEngineConfigurator;

import static javax.websocket.ContainerProvider.getWebSocketContainer;

class FlowCollectorSession extends Thread {

    private final String                hostAddress;
    private final SslEngineConfigurator sslEngineConfigurator;

    private Session session;

    FlowCollectorSession(String hostAddress, SslEngineConfigurator sslEngineConfigurator) {
        this.hostAddress = hostAddress;
        this.sslEngineConfigurator = sslEngineConfigurator;
    }

    /*
     * Creates URI from specified hostAddress and connects to it with websocket client.
     * ClientManager::connectToServer will start each web-socket client in context of current thread
     * so they all will work simultaneously.
     */
    @Override
    public void run() {
        try {
            ClientManager container = (ClientManager) getWebSocketContainer();
            if (sslEngineConfigurator != null) {
                container.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
            }
            session = container.connectToServer(Endpoint.class, URI.create(hostAddress));
        } catch (IOException | IllegalArgumentException e) {
            Loggers.system.error("Malformed URI: ", e);
        } catch (DeploymentException e) {
            Loggers.system.error("Error Starting Session: ", e);
        }
    }

    @Override
    public void interrupt() {
        try {
            session.close();
        } catch (IOException e) {
            Loggers.system.error("Error Closing Session: ", e);
        }
    }
}
