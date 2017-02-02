package stealthwatch.flowfowarder.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lancope.sw.ExternalFlowProtos;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlows;
import java.net.URI;
import java.nio.ByteBuffer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.apache.log4j.Logger;

import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static org.apache.log4j.Logger.getLogger;

/**
 * https://dzone.com/articles/sample-java-web-socket-client
 */
@ClientEndpoint
public class FlowWriter {

    private static final Logger log = getLogger(FlowWriter.class);

    private static final Object waitLock = new Object();

    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            ExtFlows extFlows = ExtFlows.parseFrom(message.array());
            for (int i = 0; i < extFlows.getFlowCount(); ++i) {
                log(extFlows.getFlow(i));
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("Unable to parse message.", e);
        }
    }

    private static void log(ExtFlow extFlow) {
        log.info(extFlow);
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("onOpen " + session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("onClose " + session + ' ' + reason);
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
