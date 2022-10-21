//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

interface Loggers {
    Logger system  = LogManager.getLogger("system");
    Logger message = LogManager.getLogger("message");
}
