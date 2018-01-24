//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import static java.lang.System.out;

/**
 * The Helper for Command Line Arguments.
 */
class CLArgsHelper {

    /**
     * Parses the command line arguments.
     *
     * @param arguments the arguments
     *
     * @return the client CLI arguments
     */
    static ClientCLIArguments parse(String... arguments) {
        ClientCLIArguments result = new ClientCLIArguments();
        for (int i = 0; i < arguments.length; ++i) {
            switch (arguments[i]) {
            case "-a":
            case "--allow-all-hosts":
                result.setBypassHostVerification(true);
                break;
            case "-h":
            case "--help":
                result.setAskingForHelp(true);
                break;
            case "-d":
            case "--debug-ssl":
                result.setDebugSSlConnection(true);
                break;
            case "-ks":
            case "--keystore":
                result.setKeyStorePath(arguments[++i]);
                break;
            case "-ksp":
            case "--keystore-password":
                result.setKeyStorePassword(arguments[++i]);
                break;
            case "-ts":
            case "--truststore":
                result.setTrustStorePath(arguments[++i]);
                break;
            case "-tsp":
            case "--truststore-password":
                result.setTrustStorePassword(arguments[++i]);
                break;
            default:
                result.addHost(arguments[i]);
                break;
            }
        }
        return result;
    }

    static void printHelp() {
        out.println("USAGE:");
        out.println("   <command> [options] hosts...");
        out.println("");
        out.println("  -h, --help                   Displays this help.");
        out.println("  -a, --allow-all-hosts        Bypass host name verification.");
        out.println("  -d, --debug-ssl              Output SSL debug information.");
        out.println("  -ks, --keystore              PKCS12 Key Store path.");
        out.println("  -ksp, --keystore-password    PKCS12 Key Store password.");
        out.println("  -ts, --truststore            PKCS12 Trust Store path.");
        out.println("  -tsp, --truststore-password  PKCS12 Trust Store password.");
        out.println("");
        out.println("hosts");
        out.println("  A space-separated list of URI paths, of the format:");
        out.println("     wss://<hostname_or_ip_address>/flowforwarder/websocket");
        out.println("");
    }
}
