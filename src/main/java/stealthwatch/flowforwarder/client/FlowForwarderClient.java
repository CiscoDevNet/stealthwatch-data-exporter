//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;

public class FlowForwarderClient {

    /** The Constant KEY_STORE_ALG. */
    private static final String KEY_STORE_ALG = "SunX509";

	/** The Constant STORE_TYPE. */
	private static final String STORE_TYPE = "PKCS12";

	private final List<FlowCollector> flowCollectors = new ArrayList<>();

    /** The command line interface arguments. */
    private ClientCLIArguments cliArgs;

    /** The ssl engine configurator. */
    private SslEngineConfigurator sslEngineConfigurator;

    public FlowForwarderClient(ClientCLIArguments cliArgs) {
    	this.cliArgs = cliArgs;

    	if (cliArgs.isNeedSecureConnection()) {
			if(cliArgs.isDebugSSlConnection()) {
				System.setProperty("javax.net.debug", "all");
			}
    		System.setProperty(SslContextConfigurator.KEY_FACTORY_MANAGER_ALGORITHM, KEY_STORE_ALG);
    		System.setProperty(SslContextConfigurator.KEY_STORE_FILE, cliArgs.getKeyStorePath());
    		System.setProperty(SslContextConfigurator.KEY_STORE_TYPE, STORE_TYPE);
    		System.setProperty(SslContextConfigurator.TRUST_STORE_FILE, cliArgs.getTrustStorePath());
    		System.setProperty(SslContextConfigurator.TRUST_STORE_TYPE, STORE_TYPE);
    		SslContextConfigurator defaultConfig = new SslContextConfigurator();
	    	String tsp = cliArgs.getTrustStorePassword();
	    	if (tsp != null && !tsp.isEmpty()) {
	    		defaultConfig.setTrustStorePassword(tsp);
	    		System.setProperty(SslContextConfigurator.TRUST_STORE_PASSWORD, tsp);
	    	}
	    	String ksp = cliArgs.getKeyStorePassword();
	    	if (ksp != null && !ksp.isEmpty()) {
	    		defaultConfig.setKeyStorePassword(ksp);
	    		System.setProperty(SslContextConfigurator.KEY_STORE_PASSWORD, ksp);
	    	}
	    	sslEngineConfigurator = new SslEngineConfigurator(defaultConfig, true, false, false);
	    	sslEngineConfigurator.setHostVerificationEnabled(!cliArgs.isBypassHostVerification());
    	}
    }

	/**
	 * Starts flow collectors.
	 * Each flow collector will be started in separate thread.
	 */
    public void forwardFlows(){
        for (String host : cliArgs.getHosts()) {
            FlowCollector flowCollector = host.startsWith("wss") ? new FlowCollector(host, sslEngineConfigurator) : new FlowCollector(host);
            flowCollectors.add(flowCollector);
            flowCollector.startSession();
        }
    }

	/**
	 * Stops flow collectors.
	 */
    public void stop(){
        for (FlowCollector flowCollector : flowCollectors) {
            flowCollector.closeSession();
        }
    }

}
