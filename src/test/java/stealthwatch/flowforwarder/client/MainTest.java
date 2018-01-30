//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import org.junit.Test;
import stealthwatch.flowforwarder.client.Main.Options;
import stealthwatch.flowforwarder.client.utils.ResourceUtil;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {

    private final String ksPath = ResourceUtil.getPath("ssl/client.p12");
    private final String tsPath = ResourceUtil.getPath("ssl/truststore.p12");

    private Options options;

    @Test
    public void testAskingForHelp() {
        options = Main.parse("-h");
        assertTrue(options.askingForHelp);

        options = Main.parse("--help");
        assertTrue(options.askingForHelp);
    }

    @Test
    public void testNoHosts() {
        options = Main.parse("-d");
        assertFalse(options.validate().isEmpty());
    }

    @Test
    public void testMultipleHosts() {
        options = Main.parse("ws::10.0.37.1:8092", "ws::10.0.37.2:8092", "ws::10.37.3:8092");
        assertEquals(asList("ws::10.0.37.1:8092", "ws::10.0.37.2:8092", "ws::10.37.3:8092"),
                     options.hosts);
    }

    @Test
    public void testInsecureConnection() {
        options = Main.parse("ws::localhost:8092");
        assertTrue(options.validate().isEmpty());
    }

    @Test
    public void testSecureConnection() {
        options = Main.parse("-ks", ksPath, "-ksp", "pw.client-export",
                             "-ts", tsPath, "-tsp", "pw.trust",
                             "wss://10.0.37.30/flowforwarder/websocket",
                             "wss://10.0.37.31/flowforwarder/websocket");

        assertEquals(ksPath, options.keyStorePath);
        assertEquals("pw.client-export", options.keyStorePassword);
        assertEquals(tsPath, options.trustStorePath);
        assertEquals("pw.trust", options.trustStorePassword);
        assertTrue(options.validate().isEmpty());
    }

    @Test
    public void testSecureConnectionInvalidArgs() {
        String[] invalidKsArgs = {"-ks", "/some/path", "-ksp", "some-password",
                                  "-ts", tsPath, "-tsp", "pw.trust",
                                  "wss://10.0.37.30/flowforwarder/websocket"};
        options = Main.parse(invalidKsArgs);
        assertEquals("/some/path", options.keyStorePath);
        assertFalse(options.validate().isEmpty());
        assertEquals(singletonList("Unable to load: /some/path"), options.validate());

        invalidKsArgs[0] = "--keystore";
        options = Main.parse(invalidKsArgs);
        assertEquals("/some/path", options.keyStorePath);
        assertFalse(options.validate().isEmpty());

        String[] invalidTsArgs = {"-ks", ksPath, "-ts", "/some/path", "wss://10.0.37.30/flowforwarder/websocket"};
        options = Main.parse(invalidTsArgs);
        assertEquals("/some/path", options.trustStorePath);
        assertFalse(options.validate().isEmpty());

        invalidKsArgs[2] = "--truststore";
        options = Main.parse(invalidTsArgs);
        assertEquals("/some/path", options.trustStorePath);
        assertFalse(options.validate().isEmpty());
    }

    @Test
    public void testSecureConnectionAllValidArguments() {
        options = Main.parse("-a", "-d", "-ks", ksPath, "-ksp", "pw.client-export", "-ts",
                             tsPath, "-tsp", "pw.trust", "wss://10.0.37.30/flowforwarder/websocket");
        assertFalse(options.askingForHelp);
        assertTrue(options.bypassHostVerification);
        assertTrue(options.debugSSlConnection);
        assertEquals(ksPath, options.keyStorePath);
        assertEquals("pw.client-export", options.keyStorePassword);
        assertEquals(tsPath, options.trustStorePath);
        assertEquals("pw.trust", options.trustStorePassword);
        assertTrue(options.validate().isEmpty());
    }

    @Test
    public void testSecureConnectionAllValidArgumentsWithLongNames() {
        options = Main.parse("--allow-all-hosts", "--debug-ssl", "--keystore", ksPath,
                             "--keystore-password", "pw.client-export", "--truststore", tsPath,
                             "--truststore-password", "pw.trust", "wss://10.0.37.30/flowforwarder/websocket");
        assertFalse(options.askingForHelp);
        assertTrue(options.bypassHostVerification);
        assertTrue(options.debugSSlConnection);
        assertEquals(ksPath, options.keyStorePath);
        assertEquals("pw.client-export", options.keyStorePassword);
        assertEquals(tsPath, options.trustStorePath);
        assertEquals("pw.trust", options.trustStorePassword);
        assertTrue(options.validate().isEmpty());
    }
}
