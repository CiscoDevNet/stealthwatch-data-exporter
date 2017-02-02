package stealthwatch.flowfowarder.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlows;
import java.nio.ByteBuffer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;
import static stealthwatch.flowfowarder.client.ExtFlowFunctions.fromFlowExtToString;

@ClientEndpoint
public class MessageHandler {

    private static final Logger log = getLogger(MessageHandler.class);

    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            for (ExtFlow extFlow : ExtFlows.parseFrom(message.array()).getFlowList()) {
                log.info(fromFlowExtToString(extFlow));
            }
        } catch (InvalidProtocolBufferException e) {
            log.error("Unable to parse message.", e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("onOpen " + session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("onClose " + session + ' ' + reason);
    }
}
