//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

/**
 * The Helper for Command Line Arguments.
 */
public class CLArgsHelper {

	/**
	 * Parses the command line arguments.
	 *
	 * @param arguments the arguments
	 * @return the client CLI arguments
	 */
	public static ClientCLIArguments parse(String[] arguments) {
		ClientCLIArguments result = new ClientCLIArguments();
		if (arguments != null && arguments.length > 0) {
			for (int idx = 0; idx < arguments.length; idx++) {
				String arg = arguments[idx];
				switch (arg) {
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
						result.setKeyStorePath(arguments[++idx]);
						break;
					case "-ksp":
					case "--keystore-password":
						result.setKeyStorePassword(arguments[++idx]);
						break;
					case "-ts":
					case "--truststore":
						result.setTrustStorePath(arguments[++idx]);
						break;
					case "-tsp":
					case "--truststore-password":
						result.setTrustStorePassword(arguments[++idx]);
						break;
					default:
						result.addHost(arg);
						break;
				}
			}
		}
		return result;
	}

	/**
	 * Prints the help.
	 */
	public static void printHelp() {
		System.out.println("USAGE:");
		System.out.println("   <command> [options] hosts...\n");
		System.out.println("OPTIONS:");
		System.out.println("  The following options are standalone (don't need any extra paramenter)");
		System.out.println("   -h, --help                       Displays this help info. Does not continue the connection.");
		System.out.println("   -a, --allow-all-hosts            Bypasses the host name verrification");
		System.out.println("   -d, --debug-ssl            		Enables debug information for ssl connection");
		System.out.println("  The following options most be used with a value immediately following the option (separated by a space):");
		System.out.println("     --flag value");
		System.out.println("   -ks, --keystore                  The path to the PKCS12 Key Store (client keystore).");
		System.out.println("   -ksp, --keystore-password        The password for the key store.");
		System.out.println("   -ts, --truststore                The path to the PKCS12 Trust Store (other types of truststores doesn't work).");
		System.out.println("   -tsp, --truststore-password      The password for the truststore.\n");
		System.out.println("HOSTS:");
		System.out.println("  A space-separated list of URI paths.");
		System.out.println("  Same SSL settings will be used for all secure connections.");
	}
}