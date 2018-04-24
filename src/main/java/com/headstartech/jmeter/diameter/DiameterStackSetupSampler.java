package com.headstartech.jmeter.diameter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.jdiameter.api.ApplicationId;

import java.io.File;

/**
 * @author Per Johansson
 */
public class DiameterStackSetupSampler extends AbstractJavaSamplerClient {

    public static final String DIAMETER_STACK_KEY = "diameterStack";

    private static final String CONFIGURATION_FILE_ARG_NAME = "configuration-file";
    private static final String DICTIONARY_FILE_ARG_NAME = "dictionary-file";
    private static final String AUTHENTICATION_APPLICATION_ID_ARG_NAME = "authentication-application-id";
    private static final String VENDOR_ID_ARG_NAME= "vendor-id";

    private static final String CONFIGURATION_FILE_DEFAULT = "conf/client-jdiameter-config.xml";
    private static final String DICTIONARY_FILE_DEFAULT = "conf/dictionary.xml";
    private static final Long AUTHENTICATION_APPLICATION_ID_DEFAULT = 100L;
    private static final Long VENDOR_ID_DEFAULT = 100L;

    @Override
    public Arguments getDefaultParameters() {

        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(CONFIGURATION_FILE_ARG_NAME,CONFIGURATION_FILE_DEFAULT);
        defaultParameters.addArgument(DICTIONARY_FILE_ARG_NAME,DICTIONARY_FILE_DEFAULT);
        defaultParameters.addArgument(AUTHENTICATION_APPLICATION_ID_ARG_NAME,String.valueOf(AUTHENTICATION_APPLICATION_ID_DEFAULT));
        defaultParameters.addArgument(VENDOR_ID_ARG_NAME,String.valueOf(VENDOR_ID_DEFAULT));

        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.setIgnore();

        String configurationFile = context.getParameter(CONFIGURATION_FILE_ARG_NAME, CONFIGURATION_FILE_DEFAULT);
        String dictionaryFile = context.getParameter(DICTIONARY_FILE_ARG_NAME, DICTIONARY_FILE_DEFAULT);
        ApplicationId authAppId = ApplicationId.createByAuthAppId(context.getLongParameter(AUTHENTICATION_APPLICATION_ID_ARG_NAME, AUTHENTICATION_APPLICATION_ID_DEFAULT));

        DiameterStackConfiguration conf = new DiameterStackConfiguration(new File(configurationFile), new File(dictionaryFile), authAppId);
        try {
            DiameterStack diameterStack = new DiameterStack(conf);
            diameterStack.start();
            context.getJMeterProperties().put(DIAMETER_STACK_KEY, diameterStack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
