package stealthwatch.flowfowarder.client;

import java.net.URI;
import java.nio.ByteBuffer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import static javax.websocket.ContainerProvider.getWebSocketContainer;

/**
 * https://dzone.com/articles/sample-java-web-socket-client
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@ClientEndpoint
public class FlowWriter {
    private static final Object waitLock = new Object();

    @OnMessage
    public void onMessage(ByteBuffer message) {
        System.out.println("Received msg: " + message);
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen " + session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("onClose " + session + ' ' + reason);
    }

    private static void waitForTerminateSignal() {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void main(String... args) throws Exception {
        Session session = null;
        try {
            session = getWebSocketContainer().connectToServer(FlowWriter.class,
                                                              URI.create("ws://10.0.37.30:8092/websocket"));
            waitForTerminateSignal();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
