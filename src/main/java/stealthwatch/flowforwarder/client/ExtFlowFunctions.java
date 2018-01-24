//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import stealthwatch.flowforwarder.client.FieldFormatter.FieldFormatterType;

class ExtFlowFunctions {

    /**
     * Formats flow with specified formatter.
     *
     * @param extFlow flow which should be formatted
     *
     * @return the resulting string
     */
    static String fromFlowExtToString(ExtFlow extFlow) {
        MessageFormatter formatter = new MessageFormatter(extFlow);
        formatter.addNameFormatter("start_active_usec", FieldFormatterType.DATE);
        formatter.addNameFormatter("last_active_usec", FieldFormatterType.DATE);
        formatter.addTypeFormatter(".com.lancope.sw.IPAddress", FieldFormatterType.IP_ADDRESS);
        formatter.addTypeFormatter(".com.lancope.sw.MacAddress", FieldFormatterType.MAC_ADDRESS);
        return formatter.format();
    }
}
