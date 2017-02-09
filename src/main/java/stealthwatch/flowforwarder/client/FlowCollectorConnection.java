package stealthwatch.flowforwarder.client;

import java.io.IOException;
import java.net.URI;
import javax.websocket.DeploymentException;
import javax.websocket.Session;

import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static stealthwatch.flowforwarder.client.Loggers.system;

class FlowCollectorConnection extends Thread {
    private final String         host;
    private final SocketProtocol protocol;

    private Session session;

    FlowCollectorConnection(String host, SocketProtocol protocol) {
        this.host = host;
        this.protocol = protocol;
    }

    private URI uri() {
        return URI.create("ws://" + host + ':' + protocol.port() + "/websocket");
    }

    @Override
    public void run() {
        try {
            session = getWebSocketContainer().connectToServer(MessageHandler.class, uri());
            system.info("Connected to " + host + " using " + protocol);
        } catch (IOException | DeploymentException e) {
            system.error("Error Starting Session", e);
            close();
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            system.error("Unable to close session " + session.getId(), e);
        }
    }
}
