package stealthwatch.flowforwarder.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import stealthwatch.flowforwarder.client.utils.ResourceUtil;


import java.util.Arrays;

/**
 * The Unit Test for CLArgsHelper.
 */
public class CLArgsHelperTest {

    private static final String SECURE_HOST = "wss://10.0.37.30";
    private static final String UNSECURE_HOST = "10.0.37.1";

    /**
     * Test no arguments.
     */
    @Test
    public void testNoArguments() {
        String[] args = null;
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertFalse(result.isAskingForHelp());
        assertFalse(result.isValid());

        args = new String[0];
        result = CLArgsHelper.parse(args);
        assertFalse(result.isAskingForHelp());
        assertFalse(result.isValid());
    }

    /**
     * Test asking for help.
     */
    @Test
    public void testAskingForHelp() {
        String[] args = {"-h"};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertTrue(result.isAskingForHelp());
        assertFalse(result.isValid());

        args[0] = "--help";
        result = CLArgsHelper.parse(args);
        assertTrue(result.isAskingForHelp());
        assertFalse(result.isValid());
    }

    /**
     * Test no hosts.
     */
    @Test
    public void testNoHosts() {
        String[] args = {"-d"};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertFalse(result.isValid());
    }

    /**
     * Test args with multiple hosts.
     */
    @Test
    public void testMultipleHosts() {
        String[] args = {UNSECURE_HOST, UNSECURE_HOST, UNSECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertEquals(Arrays.asList(args), result.getHosts());
    }

    /**
     * Test unsecure connection.
     */
    @Test
    public void testUnsecureConnection() {
        String[] args = {UNSECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertTrue(result.isValid());
    }

    /**
     * Test secure connection.
     */
    @Test
    public void testSecureConnection() {
        String ksPath = ResourceUtil.getPath("ssl/client.p12");
        String ksPass = "pw.client-export";
        String tsPath = ResourceUtil.getPath("ssl/truststore.p12");
        String tsPass = "pw.trust";

        String[] args = {"-ks", ksPath, "-ksp", ksPass, "-ts", tsPath, "-tsp", tsPass, SECURE_HOST, SECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertSecureArgs(result, ksPath, ksPass, tsPath, tsPass);
        assertTrue(result.isValid());
    }

    /**
     * Test default secure params.
     */
    @Test
    public void testDefaultSecureParams() {
        String[] args = {SECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertSecureArgs(result, "ssl/client.p12", "pw.client-export", "ssl/truststore.p12", "pw.trust");
    }


    /**
     * Test secure connection invalid args.
     */
    @Test
    public void testSecureConnectionInvalidArgs() {
        String validKsPath = ResourceUtil.getPath("ssl/client.p12");
        String validTsPath = ResourceUtil.getPath("ssl/truststore.p12");
        String invalidPath = "/some/path";

        String[] invalidKsArgs = {"-ks", invalidPath, "-ts", validTsPath, SECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(invalidKsArgs);
        assertEquals(invalidPath, result.getKeyStorePath());
        assertFalse(result.isValid());

        invalidKsArgs[0] = "--keystore";
        result = CLArgsHelper.parse(invalidKsArgs);
        assertEquals(invalidPath, result.getKeyStorePath());
        assertFalse(result.isValid());

        String[] invalidTsArgs = {"-ks", validKsPath, "-ts", invalidPath, SECURE_HOST};
        result = CLArgsHelper.parse(invalidTsArgs);
        assertEquals(invalidPath, result.getTrustStorePath());
        assertFalse(result.isValid());

        invalidKsArgs[2] = "--truststore";
        result = CLArgsHelper.parse(invalidTsArgs);
        assertEquals(invalidPath, result.getTrustStorePath());
        assertFalse(result.isValid());
    }

    /**
     * Test secure connection all valid args
     */
    @Test
    public void testSecureConnectionAllValidArguments() {
        String ksPath = ResourceUtil.getPath("ssl/client.p12");
        String ksPass = "pw.client-export";
        String tsPath = ResourceUtil.getPath("ssl/truststore.p12");
        String tsPass = "pw.trust";

        String[] args = {"-a", "-d", "-ks", ksPath, "-ksp", ksPass, "-ts", tsPath, "-tsp", tsPass, SECURE_HOST};
        assertAllSecureArgs(args, ksPath, ksPass, tsPath, tsPass);

        String[] argsFullNames = {"--allow-all-hosts", "--debug-ssl", "--keystore", ksPath, "--keystore-password", ksPass,
                "--truststore", tsPath, "--truststore-password", tsPass, SECURE_HOST};
        assertAllSecureArgs(argsFullNames, ksPath, ksPass, tsPath, tsPass);
    }

    private void assertAllSecureArgs(String[] args, String ksPath, String ksPass, String tsPath, String tsPass) {
        ClientCLIArguments result = CLArgsHelper.parse(args);
        assertFalse(result.isAskingForHelp());
        assertTrue(result.isBypassHostVerification());
        assertTrue(result.isDebugSSlConnection());
        assertSecureArgs(result, ksPath, ksPass, tsPath, tsPass);
        assertTrue(result.isValid());
    }

    private void assertSecureArgs(ClientCLIArguments result, String ksPath, String ksPass, String tsPath, String tsPass) {
        assertEquals(ksPath, result.getKeyStorePath());
        assertEquals(ksPass, result.getKeyStorePassword());
        assertEquals(tsPath, result.getTrustStorePath());
        assertEquals(tsPass, result.getTrustStorePassword());
    }

}
