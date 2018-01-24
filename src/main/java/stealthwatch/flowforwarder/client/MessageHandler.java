//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlows;
import java.nio.ByteBuffer;
import java.util.Date;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * WebSocket client configuration using javax.websocket (glassfish.tyrus as implementation)
 */
@ClientEndpoint
public class MessageHandler {

    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            for (ExtFlow extFlow : ExtFlows.parseFrom(message.array()).getFlowList()) {
                Loggers.message.info(">>> " + ExtFlowFunctions.fromFlowExtToString(extFlow));
            }
        } catch (InvalidProtocolBufferException e) {
            Loggers.system.info("Unable to parse message.", e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        Loggers.system.info("Socket Opened at  " + new Date().toString() + " " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Loggers.system.info("Socket Closed  at  " + new Date().toString() + " " + session.getId());
    }
}
