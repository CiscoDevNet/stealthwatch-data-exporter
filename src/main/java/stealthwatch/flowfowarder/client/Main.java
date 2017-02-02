package stealthwatch.flowfowarder.client;

import java.net.URI;
import javax.websocket.Session;

import static javax.websocket.ContainerProvider.getWebSocketContainer;

public class Main {
    private static final Object waitLock = new Object();

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
            session = getWebSocketContainer().connectToServer(MessageHandler.class,
                                                              URI.create("ws://10.0.37.30:8092/websocket"));
            waitForTerminateSignal();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
