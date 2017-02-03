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

import static stealthwatch.flowfowarder.client.ExtFlowFunctions.fromFlowExtToString;

@ClientEndpoint
public class MessageHandler {


    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            for (ExtFlow extFlow : ExtFlows.parseFrom(message.array()).getFlowList()) {
                Loggers.message.info(">>> " + fromFlowExtToString(extFlow));
            }
        } catch (InvalidProtocolBufferException e) {
            Loggers.system.info("Unable to parse message.", e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        Loggers.system.info("onOpen " + session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Loggers.system.info("onClose " + session + ' ' + reason);
    }
}
