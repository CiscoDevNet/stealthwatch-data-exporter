//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
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
import stealthwatch.protobuf.GeneratedMessageFormatter;

import static java.lang.String.format;

/**
 * WebSocket client configuration using javax.websocket (glassfish.tyrus as implementation)
 */
@ClientEndpoint(configurator = EndpointConfigurator.class)
public class Endpoint {

    private final GeneratedMessageFormatter formatter = new GeneratedMessageFormatter();

    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            for (ExtFlow extFlow : ExtFlows.parseFrom(message.array()).getFlowList()) {
                Loggers.message.info(formatter.format(extFlow));
            }
        } catch (InvalidProtocolBufferException e) {
            Loggers.system.info("Unable to parse message.", e);
        }
    }

    @OnOpen
    public static void onOpen(Session session) {
        Loggers.system.info("Socket Opened at  " + new Date() + ' ' + session.getId());
    }

    @OnClose
    public static void onClose(Session session, CloseReason reason) {
        Loggers.system.info(format("Session %s closed, %s", session.getId(), reason));
    }
}
