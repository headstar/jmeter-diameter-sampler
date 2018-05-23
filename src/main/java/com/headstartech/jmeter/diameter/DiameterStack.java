package com.headstartech.jmeter.diameter;

import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;
import org.mobicents.diameter.dictionary.AvpDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DiameterStack {

    private static final Logger logger = LoggerFactory.getLogger(DiameterStack.class);

    private final Stack stack;
    private final SessionFactory sessionFactory;
    private final DiameterHelper diameterHelper;

    public DiameterStack(DiameterStackConfiguration diameterStackConfiguration) throws Exception {
        stack = new StackImpl();
        sessionFactory = stack.init(createConfiguration(diameterStackConfiguration.getJdiameterConfigFile()));
        AvpDictionary dictionary = AvpDictionary.INSTANCE;
        dictionary.parseDictionary(diameterStackConfiguration.getDictionaryFile().getPath());
        diameterHelper = new DiameterHelper(dictionary);

        stack.getMetaData().getLocalPeer().getCommonApplications().add(diameterStackConfiguration.getApplicationId());
        Set<ApplicationId> appIds = stack.getMetaData().getLocalPeer().getCommonApplications();

        logger.info("Diameter Stack  :: Supporting " + appIds.size() + " applications.");
        for (org.jdiameter.api.ApplicationId x : appIds) {
            logger.info("Diameter Stack  :: Common :: " + x);
        }

        // Register network req listener, even though we wont receive requests
        // this has to be done to inform stack that we support application
        Network network = stack.unwrap(Network.class);
        network.addNetworkReqListener(new NetworkReqListener() {

            @Override
            public Answer processRequest(Request request) {
                //this won't be called.
                return null;
            }
        }, diameterStackConfiguration.getApplicationId()); //passing our example app id.
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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public DiameterHelper getDiameterHelper() {
        return diameterHelper;
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