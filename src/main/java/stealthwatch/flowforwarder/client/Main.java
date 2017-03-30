//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

public class Main {

    /*
    * waitLock used here as replacement of endless loop
    * */
    private static final Object waitLock = new Object();

    private static void waitForTerminateSignal(FlowForwarderClient client) throws Exception {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException e) {
                Loggers.system.error("Exception occurred: ", e);
            } finally {
                client.stop();
            }
        }
    }

    public static void main(String... arguments) throws Exception {
        ClientCLIArguments cliArgs = CLArgsHelper.parse(arguments);

        if (!cliArgs.isValid()) {
        	CLArgsHelper.printHelp();
            System.exit(1);
        }

        FlowForwarderClient client = new FlowForwarderClient(cliArgs);
        client.forwardFlows();
        waitForTerminateSignal(client);
    }

}