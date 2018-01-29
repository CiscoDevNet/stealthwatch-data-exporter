//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import java.util.Collection;
import java.util.Iterator;

import static java.util.Arrays.asList;

public class Main {

    private static final String USAGE    =
            "usage:   <command> [options] hosts..." +
            '\n' +
            "\n  -h, --help                   Displays this help." +
            "\n  -a, --allow-all-hosts        Bypass host name verification." +
            "\n  -d, --debug-ssl              Output SSL debug information." +
            "\n  -ks, --keystore              PKCS12 Key Store path." +
            "\n  -ksp, --keystore-password    PKCS12 Key Store password." +
            "\n  -ts, --truststore            PKCS12 Trust Store path." +
            "\n  -tsp, --truststore-password  PKCS12 Trust Store password." +
            '\n' +
            "\nhosts" +
            "\n  A space-separated list of URI paths, of the format:" +
            "\n     wss://<hostname_or_ip_address>/flowforwarder/websocket" +
            '\n';
    /**
     * waitLock used here as replacement of endless loop
     */
    private static final Object waitLock = new Object();

    public static void main(String... arguments) throws Exception {
        Options options = parse(arguments);

        if (options.askingForHelp) {
            System.out.println(USAGE);
            System.exit(0);
        }

        Collection<String> errors = options.validate();
        if (!errors.isEmpty()) {
            errors.forEach(System.err::println);
            System.err.println(USAGE);
            System.exit(1);
        }

        FlowForwarderClient client = new FlowForwarderClient(options);
        client.forwardFlows();
        waitForTerminateSignal(client);
    }

    static class Options extends Configuration {
        boolean askingForHelp;
    }

    static Options parse(String... arguments) {
        Options options = new Options();
        for (Iterator<String> args = asList(arguments).iterator(); args.hasNext(); ) {
            String next = args.next();
            switch (next) {
            case "-a":
            case "--allow-all-hosts":
                options.bypassHostVerification = true;
                continue;
            case "-h":
            case "--help":
                options.askingForHelp = true;
                continue;
            case "-d":
            case "--debug-ssl":
                options.debugSSlConnection = true;
                continue;
            case "-ks":
            case "--keystore":
                options.keyStorePath = args.hasNext() ? args.next() : "";
                continue;
            case "-ksp":
            case "--keystore-password":
                options.keyStorePassword = args.hasNext() ? args.next() : "";
                continue;
            case "-ts":
            case "--truststore":
                options.trustStorePath = args.hasNext() ? args.next() : "";
                continue;
            case "-tsp":
            case "--truststore-password":
                options.trustStorePassword = args.hasNext() ? args.next() : "";
                continue;
            default:
                options.hosts.add(next);
            }
        }
        return options;
    }

    private static void waitForTerminateSignal(FlowForwarderClient client) {
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
}
