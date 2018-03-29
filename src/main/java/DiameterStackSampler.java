import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * @author Per Johansson
 */
public class DiameterStackSampler extends AbstractJavaSamplerClient {

    private static final String METHOD_TAG = "method";
    private static final String ARG1_TAG = "arg1";
    private static final String ARG2_TAG = "arg2";

    @Override
    public Arguments getDefaultParameters() {

        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(METHOD_TAG,"test");
        defaultParameters.addArgument(ARG1_TAG,"arg1");
        defaultParameters.addArgument(ARG2_TAG,"arg2");

        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        getNewLogger().info("Running!");
        SampleResult result = new SampleResult();
        result.setIgnore();
        return result;
    }
}
