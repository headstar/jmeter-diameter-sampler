package com.headstartech.jmeter.diameter;

import org.jdiameter.api.ApplicationId;
import org.junit.Test;

import java.io.File;

/**
 * @author Per Johansson
 */
public class DiameterStackTest {

    @Test
    public void canCreateStartStopAndDestroy() throws Exception {
        int commandCode = 100;
        ApplicationId authAppId = ApplicationId.createByAuthAppId(1);
        long vendorId = 1;
        String serverHost = "localhost";
        Integer serverPort = 3688;

        File configurationFile = new File(getClass().getClassLoader().getResource("client-jdiameter-config.xml").getFile());

        String serverURI =  "aaa://" + serverHost + ":" + serverPort;
        DiameterStackConfiguration diameterStackConfiguration = new DiameterStackConfiguration(commandCode, authAppId, vendorId, serverURI, configurationFile);
        DiameterStack diameterStack = new DiameterStack(diameterStackConfiguration);
        diameterStack.start();
        diameterStack.stop();
        diameterStack.destroy();
    }
}