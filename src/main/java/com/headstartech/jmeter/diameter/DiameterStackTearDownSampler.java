package com.headstartech.jmeter.diameter;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;

import static com.headstartech.jmeter.diameter.DiameterStackSetupSampler.DIAMETER_STACK_KEY;

/**
 * @author Per Johansson
 */
public class DiameterStackTearDownSampler extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        DiameterStack diameterStack = (DiameterStack) context.getJMeterProperties().get(DIAMETER_STACK_KEY);
        if(diameterStack != null) {
            try {
                diameterStack.stop();
            } catch (InternalException e) {
                // ignore
            } catch (IllegalDiameterStateException e) {
                // ignore
            } finally {
                diameterStack.destroy();
            }
        }
        SampleResult sampleResult = new SampleResult();
        sampleResult.setIgnore();
        return sampleResult;
    }
}
