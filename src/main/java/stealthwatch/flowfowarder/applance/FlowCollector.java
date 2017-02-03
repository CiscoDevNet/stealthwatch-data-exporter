package stealthwatch.flowfowarder.applance;

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
    private Session session;

    public FlowCollector(String ipAddress, SocketProtocol protocol) {
        this.ipAddress = ipAddress;
        this.port = protocol.getPort();
    }

    public void startSession() {
        try {
            this.session = getWebSocketContainer().connectToServer(MessageHandler.class,
                    URI.create("ws://" + ipAddress + ":" + port + "/websocket"));
        } catch (IOException | DeploymentException e){
            Loggers.system.error("Error Starting Session");
        }
    }

    public void closeSession() {
        try {
            session.close();
        } catch (IOException e){
            Loggers.system.error("Error Closing Session");
        }
    }
}
