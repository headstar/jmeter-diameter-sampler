package com.headstartech.jmeter.diameter;

import org.jdiameter.api.*;
import org.jdiameter.client.impl.StackImpl;
import org.jdiameter.client.impl.helpers.XMLConfiguration;
import org.mobicents.diameter.dictionary.AvpDictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class DiameterStack {

    private final Stack stack;
    private final SessionFactory sessionFactory;
    private final DiameterHelper diameterHelper;

    public DiameterStack(DiameterStackConfiguration diameterStackConfiguration) throws Exception {
        stack = new StackImpl();
        sessionFactory = stack.init(createConfiguration(diameterStackConfiguration.getJdiameterConfigFile()));
        AvpDictionary dictionary = AvpDictionary.INSTANCE;
        dictionary.parseDictionary(diameterStackConfiguration.getDictionaryFile().getPath());
        diameterHelper = new DiameterHelper(dictionary);
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