package stealthwatch.flowfowarder.applance;

import stealthwatch.flowfowarder.client.FlowCollectorSession;
import stealthwatch.flowfowarder.client.Loggers;
import stealthwatch.flowfowarder.client.MessageHandler;
import stealthwatch.flowfowarder.client.SocketProtocol;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import java.io.IOException;
import java.net.URI;

import static javax.websocket.ContainerProvider.getWebSocketContainer;


public class FlowCollector {
    private final String ipAddress;
    private final int port;
    private FlowCollectorSession session;

    public FlowCollector(String ipAddress, SocketProtocol protocol) {
        this.ipAddress = ipAddress;
        this.port = protocol.getPort();
    }

    public void startSession() {
        this.session = new FlowCollectorSession(ipAddress, port);
        session.run();
    }

    public void closeSession() {
        session.stop();
    }
}
