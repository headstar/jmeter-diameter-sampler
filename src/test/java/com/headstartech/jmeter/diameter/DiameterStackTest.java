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

        File configurationFile = new File(getClass().getClassLoader().getResource("client-jdiameter-config.xml").getFile());
        File dictionaryFile = new File(getClass().getClassLoader().getResource("dictionary.xml").getFile());
        ApplicationId authAppId = ApplicationId.createByAuthAppId(1);

        DiameterStackConfiguration diameterStackConfiguration = new DiameterStackConfiguration(
                configurationFile, dictionaryFile, authAppId);

        DiameterStack diameterStack = new DiameterStack(diameterStackConfiguration);
        diameterStack.start();
        diameterStack.stop();
        diameterStack.destroy();
    }
}
