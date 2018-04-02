package com.headstartech.jmeter.diameter;

import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class DiameterStack {

    private final Stack stack;
    private final SessionFactory sessionFactory;
    private final DiameterStackConfiguration diameterStackConfiguration;

    public DiameterStack(DiameterStackConfiguration diameterStackConfiguration) throws Exception {
        this.diameterStackConfiguration = diameterStackConfiguration;

        // TODO: using server stack as in https://github.com/RestComm/jdiameter/blob/master/examples/guide1/src/main/java/org/example/client/ExampleClient.java ??
        stack = new StackImpl();
        sessionFactory = stack.init(createConfiguration(diameterStackConfiguration.getJdiameterConfigFile()));

        // Register network request listener, even though we wont receive requests
        // this has to be done to inform stack that we support application
        Network network = stack.unwrap(Network.class);
        network.addNetworkReqListener(request -> null, diameterStackConfiguration.getAppId());
    }

    public void start() throws InternalException, IllegalDiameterStateException {
        stack.start();
    }

    public void stop() throws InternalException, IllegalDiameterStateException {
        stack.stop(10, TimeUnit.SECONDS, DisconnectCause.REBOOTING);
    }

    public void destroy() {
        stack.destroy();
    }

    public DiameterStackConfiguration getDiameterStackConfiguration() {
        return diameterStackConfiguration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Configuration createConfiguration(File configFile) throws Exception {
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            return new XMLConfiguration(is);
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }
}