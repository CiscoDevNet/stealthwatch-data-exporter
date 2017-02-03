package stealthwatch.flowfowarder.applance;

import stealthwatch.flowfowarder.client.MessageHandler;
import stealthwatch.flowfowarder.client.SocketProtocol;

import javax.websocket.Session;

import java.net.URI;

import static javax.websocket.ContainerProvider.getWebSocketContainer;


public class FlowCollector {
    private final String ipAddress;
    private final int port;
    private Session session;

    public FlowCollector(String ipAddress, SocketProtocol protocol) throws Exception{
        this.ipAddress = ipAddress;
        this.port = protocol.getPort();
        startSession();
    }

    private void startSession() throws Exception{
        this.session = getWebSocketContainer().connectToServer(MessageHandler.class,
                URI.create("ws://" + ipAddress + ":" + port + "/websocket"));
    }

    public void closeSession() throws Exception{
        session.close();
    }
}
