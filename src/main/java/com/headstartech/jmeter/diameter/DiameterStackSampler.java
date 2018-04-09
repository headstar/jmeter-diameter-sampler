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
public class DiameterStackSampler extends AbstractJavaSamplerClient {

    public static final String DIAMETER_STACK_KEY = "diameterStack";

    private static final String COMMAND_CODE_ARG_NAME = "command-code";
    private static final String AUTHENTICATION_APPLICATION_ID_ARG_NAME = "authentication-application-id";
    private static final String VENDOR_ID_ARG_NAME= "vendor-id";
    private static final String SERVER_HOST_ARG_NAME = "server-host";
    private static final String SERVER_PORT_ARG_NAME = "server-port";
    private static final String CONFIGURATION_FILE_ARG_NAME = "configuration-file";
    private static final String REALM_ARG_NAME = "realm";

    private static final Integer COMMAND_CODE_DEFAULT = 100;
    private static final Long AUTHENTICATION_APPLICATION_ID_DEFAULT = 100L;
    private static final Long VENDOR_ID_DEFAULT = 100L;
    private static final String SERVER_HOST_DEFAULT = "127.0.0.1";
    private static final Integer SERVER_PORT_DEFAULT = 3868;
    private static final String CONFIGURATION_FILE_DEFAULT = "conf/client-jdiameter-config.xml";
    private static final String REALM_DEFAULT = "exchange.example.org";

    @Override
    public Arguments getDefaultParameters() {

        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(COMMAND_CODE_ARG_NAME,String.valueOf(COMMAND_CODE_DEFAULT));
        defaultParameters.addArgument(AUTHENTICATION_APPLICATION_ID_ARG_NAME,String.valueOf(AUTHENTICATION_APPLICATION_ID_DEFAULT));
        defaultParameters.addArgument(VENDOR_ID_ARG_NAME,String.valueOf(VENDOR_ID_DEFAULT));
        defaultParameters.addArgument(REALM_ARG_NAME, REALM_DEFAULT);
        defaultParameters.addArgument(SERVER_HOST_ARG_NAME,SERVER_HOST_DEFAULT);
        defaultParameters.addArgument(SERVER_PORT_ARG_NAME, String.valueOf(SERVER_PORT_DEFAULT));
        defaultParameters.addArgument(CONFIGURATION_FILE_ARG_NAME,CONFIGURATION_FILE_DEFAULT);

        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        getNewLogger().info("Running!");
        SampleResult result = new SampleResult();
        result.setIgnore();

        int commandCode = context.getIntParameter(COMMAND_CODE_ARG_NAME, COMMAND_CODE_DEFAULT);
        ApplicationId authAppId = ApplicationId.createByAuthAppId(context.getLongParameter(AUTHENTICATION_APPLICATION_ID_ARG_NAME, AUTHENTICATION_APPLICATION_ID_DEFAULT));
        long vendorId = context.getLongParameter(VENDOR_ID_ARG_NAME, VENDOR_ID_DEFAULT);
        String serverHost = context.getParameter(SERVER_HOST_ARG_NAME, SERVER_HOST_DEFAULT);
        Integer serverPort = context.getIntParameter(SERVER_PORT_ARG_NAME, SERVER_PORT_DEFAULT);
        String configurationFile = context.getParameter(CONFIGURATION_FILE_ARG_NAME, CONFIGURATION_FILE_DEFAULT);
        String realm = context.getParameter(REALM_ARG_NAME, REALM_DEFAULT);

        String serverURI =  "aaa://" + serverHost + ":" + serverPort;
        DiameterStackConfiguration diameterStackConfiguration = new DiameterStackConfiguration(commandCode, authAppId, vendorId, realm, serverURI, new File(configurationFile));
        try {
            DiameterStack diameterStack = new DiameterStack(diameterStackConfiguration);
            context.getJMeterProperties().put(DIAMETER_STACK_KEY, diameterStack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
