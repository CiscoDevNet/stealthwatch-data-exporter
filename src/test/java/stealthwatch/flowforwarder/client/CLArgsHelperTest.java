package stealthwatch.flowforwarder.client;

import org.junit.Test;
import stealthwatch.flowforwarder.client.utils.ResourceUtil;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CLArgsHelperTest {

    private static final String SECURE_HOST   = "wss://10.0.37.30";
    private static final String INSECURE_HOST = "10.0.37.1";

    private ClientCLIArguments arguments;
    private final String ksPath = ResourceUtil.getPath("ssl/client.p12");
    private final String tsPath = ResourceUtil.getPath("ssl/truststore.p12");

    @Test
    public void testAskingForHelp() {
        arguments = CLArgsHelper.parse("-h");
        assertTrue(arguments.isAskingForHelp());
        assertFalse(arguments.isValid());

        arguments = CLArgsHelper.parse("--help");
        assertTrue(arguments.isAskingForHelp());
        assertFalse(arguments.isValid());
    }

    @Test
    public void testNoHosts() {
        arguments = CLArgsHelper.parse("-d");
        assertFalse(arguments.isValid());
    }

    @Test
    public void testMultipleHosts() {
        arguments = CLArgsHelper.parse(INSECURE_HOST, INSECURE_HOST, INSECURE_HOST);
        assertEquals(asList(INSECURE_HOST, INSECURE_HOST, INSECURE_HOST), arguments.getHosts());
    }

    @Test
    public void testUnsecureConnection() {
        arguments = CLArgsHelper.parse(INSECURE_HOST);
        assertTrue(arguments.isValid());
    }

    @Test
    public void testSecureConnection() {
        arguments = CLArgsHelper.parse("-ks", ksPath, "-ksp", "pw.client-export", "-ts", tsPath, "-tsp",
                                       "pw.trust", SECURE_HOST, SECURE_HOST);

        assertEquals(ksPath, arguments.getKeyStorePath());
        assertEquals("pw.client-export", arguments.getKeyStorePassword());
        assertEquals(tsPath, arguments.getTrustStorePath());
        assertEquals("pw.trust", arguments.getTrustStorePassword());
        assertTrue(arguments.isValid());
    }

    @Test
    public void testSecureConnectionInvalidArgs() {
        String[] invalidKsArgs = {"-ks", "/some/path", "-ts", tsPath, SECURE_HOST};
        ClientCLIArguments result = CLArgsHelper.parse(invalidKsArgs);
        assertEquals("/some/path", result.getKeyStorePath());
        assertFalse(result.isValid());

        invalidKsArgs[0] = "--keystore";
        result = CLArgsHelper.parse(invalidKsArgs);
        assertEquals("/some/path", result.getKeyStorePath());
        assertFalse(result.isValid());

        String[] invalidTsArgs = {"-ks", ksPath, "-ts", "/some/path", SECURE_HOST};
        result = CLArgsHelper.parse(invalidTsArgs);
        assertEquals("/some/path", result.getTrustStorePath());
        assertFalse(result.isValid());

        invalidKsArgs[2] = "--truststore";
        result = CLArgsHelper.parse(invalidTsArgs);
        assertEquals("/some/path", result.getTrustStorePath());
        assertFalse(result.isValid());
    }

    @Test
    public void testSecureConnectionAllValidArguments() {
        arguments = CLArgsHelper.parse("-a", "-d", "-ks", ksPath, "-ksp", "pw.client-export", "-ts",
                                       tsPath, "-tsp", "pw.trust", SECURE_HOST);
        assertFalse(arguments.isAskingForHelp());
        assertTrue(arguments.isBypassHostVerification());
        assertTrue(arguments.isDebugSSlConnection());
        assertEquals(ksPath, arguments.getKeyStorePath());
        assertEquals("pw.client-export", arguments.getKeyStorePassword());
        assertEquals(tsPath, arguments.getTrustStorePath());
        assertEquals("pw.trust", arguments.getTrustStorePassword());
        assertTrue(arguments.isValid());
    }

    @Test
    public void testSecureConnectionAllValidArgumentsWithLongNames() {
        arguments = CLArgsHelper.parse("--allow-all-hosts", "--debug-ssl", "--keystore", ksPath,
                                       "--keystore-password", "pw.client-export", "--truststore", tsPath,
                                       "--truststore-password", "pw.trust", SECURE_HOST);
        assertFalse(arguments.isAskingForHelp());
        assertTrue(arguments.isBypassHostVerification());
        assertTrue(arguments.isDebugSSlConnection());
        assertEquals(ksPath, arguments.getKeyStorePath());
        assertEquals("pw.client-export", arguments.getKeyStorePassword());
        assertEquals(tsPath, arguments.getTrustStorePath());
        assertEquals("pw.trust", arguments.getTrustStorePassword());
        assertTrue(arguments.isValid());
    }
}
